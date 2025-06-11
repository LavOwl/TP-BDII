package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import unlp.info.bd2.utils.ToursException;

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

    @DBRef(lazy = true)
    private List<Stop> stops;

    @DBRef(lazy = true)
    private List<DriverUser> driverList = new ArrayList<DriverUser>();

    @DBRef(lazy = true)
    private List<TourGuideUser> tourGuideList = new ArrayList<TourGuideUser>();

    @DBRef(lazy = true)
    private List<Purchase> purchases = new ArrayList<Purchase>();

    public Route(String name, float price, float totalKm, int maxNumberUsers, List<Stop> stops) {
        this.name = name;
        this.price = price;
        this.totalKm = totalKm;
        this.maxNumberUsers = maxNumberUsers;
        this.stops = stops;
    }

    public void addTourGuide(TourGuideUser tourGuide){
        if(!this.tourGuideList.contains(tourGuide)){
            this.tourGuideList.add(tourGuide);
            tourGuide.addRoute(this);
        }
    }

    public void addDriver(DriverUser driverUser){
        if(!this.driverList.contains(driverUser)){
            this.driverList.add(driverUser);
            driverUser.addRoute(this);
        }
    }

    public void setTourGuideList(List<TourGuideUser> tourGuideUsers){
        for (TourGuideUser tourGuideUser : tourGuideUsers) {
            tourGuideUser.addRoute(this);
        }
        this.tourGuideList = tourGuideUsers;
    }

    public void setDriverList(List<DriverUser> driverUsers){
        for (DriverUser driverUser : driverUsers) {
            driverUser.addRoute(this);
        }
        this.driverList = driverUsers;
    }

    public void addPurchase(Purchase purchase) throws ToursException{
        if(purchases.size() < this.maxNumberUsers){
            this.purchases.add(purchase);
        }
        else{
            throw new ToursException("No se pudo hacer la compra");
        }
    }

}
