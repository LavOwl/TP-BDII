package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

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
    private List<DriverUser> driverList;

    @DBRef
    private List<TourGuideUser> tourGuideList;

    public void addTourGuide(TourGuideUser tourGuide){

    }

    public void addDriver(DriverUser driverUser){

    }

}
