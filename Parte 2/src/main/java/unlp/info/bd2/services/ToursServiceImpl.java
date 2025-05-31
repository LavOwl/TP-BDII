package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService {
    
    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException{return null;}
    
    @Override
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException{return null;}
    
    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException{return null;}
    
    @Override
    public Optional<User> getUserById(ObjectId id) throws ToursException{return null;}
    
    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException{return null;}
    
    @Override
    public User updateUser(User user) throws ToursException{return null;}
    
    @Override
    public void deleteUser(User user) throws ToursException{}
    
    @Override
    public Stop createStop(String name, String description) throws ToursException{return null;}
    
    @Override
    public List<Stop> getStopByNameStart(String name){return null;}
    
    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{return null;}
    
    @Override
    public Optional<Route> getRouteById(ObjectId id){return null;}
    
    @Override
    public List<Route> getRoutesBelowPrice(float price){return null;}
    
    @Override
    public void assignDriverByUsername(String username, ObjectId idRoute) throws ToursException{}
    
    @Override
    public void assignTourGuideByUsername(String username, ObjectId idRoute) throws ToursException{}
    
    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{return null;}
    
    @Override
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{return null;}
    
    @Override
    public Service updateServicePriceById(ObjectId id, float newPrice) throws ToursException{return null;}
    
    @Override
    public Optional<Supplier> getSupplierById(ObjectId id){return null;}
    
    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber){return null;}
    
    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, ObjectId id) throws ToursException{return null;}
    
    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException{return null;}
    
    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException{return null;}
    
    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException{return null;}
    
    @Override
    public Optional<Purchase> getPurchaseByCode(String code){return null;}
    
    @Override
    public void deletePurchase(Purchase purchase) throws ToursException{}
    
    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException{return null;}

    // CONSULTAS HQL
    
    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username){return null;}
    
    @Override
    public List<User> getUserSpendingMoreThan(float mount){return null;}
    
    @Override
    public List<User> getUsersWithNumberOfPurchases(int number){return null;}
    
    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n){return null;}
    
    @Override
    public List<Supplier> getTopNSuppliersItemsSold(int n){return null;}
    
    @Override
    public List<Purchase> getTop10MoreExpensivePurchasesWithServices(){return null;}
    
    @Override
    public List<User> getTop5UsersMorePurchases(){return null;}
    
    @Override
    public List<Route> getTop3RoutesWithMoreStops(){return null;}
    
    @Override
    public Long getCountOfPurchasesBetweenDates(Date start, Date end){return null;}
    
    @Override
    public List<Route> getRoutesWithStop(Stop stop){return null;}
    
    @Override
    public List<Purchase> getPurchaseWithService(Service service){return null;}
    
    @Override
    public Long getMaxStopOfRoutes(){return null;}
    
    @Override
    public Long getMaxServicesOfSupplier(){return null;}
    
    @Override
    public List<Route> getRoutsNotSell(){return null;}
    
    @Override
    public List<Route> getTop3RoutesWithMaxAverageRating(){return null;}
    
    @Override
    public List<Route> getRoutesWithMinRating(){return null;}
    
    @Override
    public Service getMostDemandedService(){return null;}
    
    @Override
    public Route getMostBestSellingRoute(){return null;}
    
    @Override
    public List<Service> getServiceNoAddedToPurchases(){return null;}
    
    @Override
    public List<TourGuideUser> getTourGuidesWithRating1(){return null;}
    
    @Override
    public DriverUser getDriverUserWithMoreRoutes(){return null;}
}
