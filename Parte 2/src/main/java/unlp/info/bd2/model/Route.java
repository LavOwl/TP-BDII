package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Route {

    @Id
    private ObjectId id;

    private String name;

    private float price;

    private float totalKm;

    private int maxNumberUsers;

    @DBRef
    private List<Stop> stops;

    @DBRef
    private List<DriverUser> driverList = new ArrayList<DriverUser>();

    @DBRef
    private List<TourGuideUser> tourGuideList = new ArrayList<TourGuideUser>();

    public Route(String name, float price, float totalKm, int maxNumberUsers, List<Stop> stops) {
        this.name = name;
        this.price = price;
        this.totalKm = totalKm;
        this.maxNumberUsers = maxNumberUsers;
        this.stops = stops;
    }

    public void addTourGuide(TourGuideUser tourGuide){

    }

    public void addDriver(DriverUser driverUser){

    }

}
