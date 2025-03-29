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

    public void addDriver(DriverUser driver){
        //Implement
    }

    public void addTourGuide(TourGuideUser tourGuide){
        //Implement
    }

}
