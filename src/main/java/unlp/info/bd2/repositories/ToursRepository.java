package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.utils.ToursException;
import unlp.info.bd2.model.Service;

import unlp.info.bd2.model.Route;

public interface ToursRepository {
    
    //Refactoring
    public <T> T save(T persistableObject);
    public <T> void delete(T persistableObject);
    public <T> Optional<T> getById(Class<T> clazz, Long id);

    //IVY
    public Optional<User> getUserByUsername(String username);

    public List<Stop> getStopByNameStart(String name);

    //FABRI

    public List<Route> getRoutesBelowPrice(float price);

    public void assignDriverByUsername(String username, Long idRoute) throws ToursException;

    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException;

    public List<User> getTop5UsersMorePurchases();
    
    public long getCountOfPurchasesBetweenDates(Date start, Date end);

    public List<Route> getRoutesWithStop(Stop stop);

    public Long getMaxStopOfRoutes();

    public List<Route> getRoutsNotSell();


    //FRANCO

    /** Returns the Supplier of the Authorization Number sent if it's exists. */
    public Optional<Supplier> getSupplierByAuthorizationNumber (String authorizationNumber);

    /** Returns the Service that corresponds to the name of service and his supplier ID sent if it's exists */
    public Optional<Service> getServiceByNameAndSupplierId (String name, Long id) throws ToursException;
    
    /** Returns the Purchase by the code sent */
    public Optional<Purchase> getPurchaseByCode (String code);

    //HQL SENTENCES

    //IVY
    public List<Purchase> getAllPurchasesOfUsername(String username);
    public List<User> getUserSpendingMoreThan(float amount);
    public List<Supplier> getTopNSuppliersInPurchases(int n);
    public List<Purchase> getTop10MoreExpensivePurchasesInServices();
    
    //FABRI

    //FRANCO
    /** Returns the top 3 routes with max average rating in his reviews and purchases associated */
    List<Route> getTop3RoutesWithMaxAverageRating();

    /** Return the Routes that have at least a one-star rating */
    List<Route> getRoutesWithMinRating();

    /** Returns the service that was included the most times in purchases, taking into account 
     * the quantity. */
    Service getMostDemandedService();
    
    /** Returns the services that weren´t included in any purchase */
    List<Service> getServiceNoAddedToPurchases();

    /** Returns tour guides assigned to any one-star rated tour */
    List<TourGuideUser> getTourGuidesWithRating1();
}
