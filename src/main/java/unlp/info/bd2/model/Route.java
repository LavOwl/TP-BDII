package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    //Implement
    private List<Stop> stops;

    //Implement
    private List<DriverUser> driverList;

    //Implement
    private List<TourGuideUser> tourGuideList;

    public void addDriver(DriverUser driver){

    }

    public void addTourGuide(TourGuideUser tourGuide){
        
    }

}
