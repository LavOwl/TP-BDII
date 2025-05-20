package unlp.info.bd2.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import unlp.info.bd2.utils.ToursException;

import java.util.ArrayList;
import jakarta.persistence.FetchType;

@Data
@Entity
@Table(name = "Route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "total_km", nullable = false)
    private float totalKm;

    @Column(name = "max_number_users", nullable = false)
    private int maxNumberUsers;

    @ManyToMany(cascade = {},fetch = FetchType.EAGER) //Discutible, depende de la relevancia que tengan las paradas en la aplicación real de la BD.
    @JoinTable(name = "route_stop", joinColumns = @JoinColumn(name = "route_id"), inverseJoinColumns = @JoinColumn(name = "stop_id"))
    private List<Stop> stops = new ArrayList<>();

    @ManyToMany(cascade = {},fetch = FetchType.LAZY)
    @JoinTable(name = "route_driver", joinColumns = @JoinColumn(name = "route_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "driverUser_id"))
    private List<DriverUser> driverList = new ArrayList<>();

    @ManyToMany(cascade = {},fetch = FetchType.LAZY)
    @JoinTable(name = "route_guide", joinColumns = @JoinColumn(name = "route_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "tourGuideUser_id"))
    private List<TourGuideUser> tourGuideList = new ArrayList<>();

    @OneToMany(mappedBy = "route", cascade = {}, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

    public Route() {
        // Default constructor
    }

    public Route (String name, float price, float totalKm, int maxNumberUsers, List<Stop> stops) {
        this.name = name;
        this.price = price;
        this.totalKm = totalKm;
        this.maxNumberUsers = maxNumberUsers;
        this.stops = stops;
    }

    public void addDriver(DriverUser driver){
        if (this.driverList.contains(driver)) {
            return;
        }

        this.driverList.add(driver);
    }

    public void addTourGuide(TourGuideUser tourGuide){
        if (this.tourGuideList.contains(tourGuide)) {
            return;
        }

        this.tourGuideList.add(tourGuide);
    }

    public void addPurchase(Purchase purchase) throws ToursException {
        if(this.purchases.size() < this.maxNumberUsers){
            this.purchases.add(purchase);
        }
        else{
            throw new ToursException("The route is full, you cannot add more purchases");
        }
    }
}
