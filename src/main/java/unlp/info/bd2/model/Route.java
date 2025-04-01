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

    @Column(name = "totalKm", nullable = false)
    private float totalKm;

    @Column(name = "maxNumberUsers", nullable = false)
    private int maxNumberUsers;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "route_stop", joinColumns = @JoinColumn(name = "route_id"), inverseJoinColumns = @JoinColumn(name = "stop_id"))
    private List<Stop> stops;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "route_driver", joinColumns = @JoinColumn(name = "route_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "driverUser_id"))
    private List<DriverUser> driverList;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "route_guide", joinColumns = @JoinColumn(name = "route_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "tourGuideUser_id"))
    private List<TourGuideUser> tourGuideList;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

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
        //modifique aca

        // Check if the driver is already in the list to avoid duplicates
        if (this.driverList.contains(driver)) {
            return;
        }
        // Add the driver to the list and set the route reference in the driver
        this.driverList.add(driver);

        //hasta aca la modificacion
    }

    public void addTourGuide(TourGuideUser tourGuide){
        //modifique aca

        // Check if the tour guide is already in the list to avoid duplicates
        if (this.tourGuideList.contains(tourGuide)) {
            return;
        }
        // Add the tour guide to the list and set the route reference in the tour guide
        this.tourGuideList.add(tourGuide);
        
        //hasta aca la modificacion
    }
}
