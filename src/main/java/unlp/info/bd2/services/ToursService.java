package unlp.info.bd2.services;
import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ToursService {

    //IVY
    User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException;
    DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException;
    TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException;
    Optional<User> getUserById(Long id) throws ToursException;
    Optional<User> getUserByUsername(String username) throws ToursException;
    User updateUser(User user) throws ToursException;
    void deleteUser(User user) throws ToursException;
    Stop createStop(String name, String description) throws ToursException;
    List<Stop> getStopByNameStart(String name) throws ToursException;

    //FABRI
    Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException;
    Optional<Route> getRouteById(Long id);
    List<Route> getRoutesBelowPrice(float price);
    void assignDriverByUsername(String username, Long idRoute) throws ToursException;
    void assignTourGuideByUsername(String username, Long idRoute) throws ToursException;
    Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException;
    Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException;
    Service updateServicePriceById(Long id, float newPrice) throws ToursException;
    
    //FRANCO
    /** Returns the Supplier of the ID sent if it's exists. */
    Optional<Supplier> getSupplierById (Long id);
    
    /** Returns the Supplier of the Authorization Number sent if it's exists. */
    Optional<Supplier> getSupplierByAuthorizationNumber (String authorizationNumber);
    
    /** Returns the Service that corresponds with his name and her supplier ID if it's exists.<p>
     */
    Optional<Service> getServiceByNameAndSupplierId (String name, Long id) throws ToursException;
    
    /** Creates a Purchase by the arguments sent:<p>
     * code -> String<p>
     * route -> Route<p>
     * user -> User<p>
     */
    Purchase createPurchase (String code, Route route, User user) throws ToursException;
    
    /** Creates a Purchase by the arguments sent:<p>
     * code -> String<p>
     * date -> Date<p>
     * route -> Route<p>
     * user -> User<p>
     */
    Purchase createPurchase (String code, Date date, Route route, User user) throws ToursException;
    
    /** Creates an ItemService and add's it in the given Purchase. Receives:<p>
     * service -> Service<p>
     * quantity -> int<p>
     * purchase -> Purchase<p>
     */
    ItemService addItemToPurchase (Service service, int quantity, Purchase purchase) throws ToursException;
    
    /** Returns the Purchase by the code sent */
    Optional<Purchase> getPurchaseByCode (String code);
    
    /** Deletes the Purchase given from the DB */
    void deletePurchase (Purchase purchase) throws ToursException;
    
    /** Creates a Review and add's it in the given Purchase. Receives:<p>
     * rating -> int<p>
     * comment -> String<p>
     * purchase -> Purchase<p>
     */
    Review addReviewToPurchase (int rating, String comment, Purchase purchase) throws ToursException;

    // CONSULTAS HQL

    //IVY
    List<Purchase> getAllPurchasesOfUsername(String username);
    List<User> getUserSpendingMoreThan(float mount);
    List<Supplier> getTopNSuppliersInPurchases(int n);
    List<Purchase> getTop10MoreExpensivePurchasesInServices();

    //FABRI
    List<User> getTop5UsersMorePurchases();
    long getCountOfPurchasesBetweenDates(Date start, Date end);
    List<Route> getRoutesWithStop(Stop stop);
    Long getMaxStopOfRoutes();
    List<Route> getRoutsNotSell();

    //FRANCO
    /** Returns the top 3 Routes with max average rating in his reviews and purchases associated */
    List<Route> getTop3RoutesWithMaxRating();

    /** Return the Routes that have at least a one-star rating */
    List<Route> getRoutesWithMinRating();

    /** Returns the service that was included the most times in purchases, taking into account 
     * the quantity. */
    Service getMostDemandedService();
    
    /** Returns the services that weren´t included in any purchase */
    List<Service> getServiceNoAddedToPurchases();

    /** Returns tour guides assigned to any one-star rated tour */
    List<TourGuideUser> getTourGuidesWithRating1();

    /**FALTAN (NO ESTAN EN EL CODIGO, PERO SI EN LA CONSIGNA DE ESTE AÑO)
    List<User> getUsersWithNumberOfPurchases(int number);
    List<Supplier> getTopNSuppliersItemsSold(int n);
    List<Purchase> getTop10MoreExpensivePurchasesWithServices();
    Route getMostBestSellingRoute();
    DriverUser getDriverUserWithMoreRoutes();
    */
}
