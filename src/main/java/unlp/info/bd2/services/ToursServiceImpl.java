package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.User;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService {
    private ToursRepository toursRepository;

    public ToursServiceImpl(ToursRepository toursRepository){
        this.toursRepository = toursRepository;
    }

    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException{return null;}
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException{return null;}
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException{return null;}
    public Optional<User> getUserById(Long id) throws ToursException{return null;}
    public Optional<User> getUserByUsername(String username) throws ToursException{return null;}
    public User updateUser(User user) throws ToursException{return null;}
    public void deleteUser(User user) throws ToursException{}
    public Stop createStop(String name, String description) throws ToursException{return null;}
    public List<Stop> getStopByNameStart(String name){return null;}
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{return null;}
    public Optional<Route> getRouteById(Long id){return null;}
    public List<Route> getRoutesBelowPrice(float price){return null;}
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException{}
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{}
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{return null;}
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{return null;}
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException{return null;}
    public Optional<Supplier> getSupplierById(Long id){return null;}
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber){return null;}
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException{return null;}
    public Purchase createPurchase(String code, Route route, User user) throws ToursException{return null;}
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException{return null;}
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException{return null;}
    public Optional<Purchase> getPurchaseByCode(String code){return null;}
    public void deletePurchase(Purchase purchase) throws ToursException{}
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException{return null;}

    // CONSULTAS HQL
    public List<Purchase> getAllPurchasesOfUsername(String username){return null;}
    public List<User> getUserSpendingMoreThan(float mount){return null;}
    public List<Supplier> getTopNSuppliersInPurchases(int n){return null;}
    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){return null;}
    public List<User> getTop5UsersMorePurchases(){return null;}
    public long getCountOfPurchasesBetweenDates(Date start, Date end){return 0;}
    public List<Route> getRoutesWithStop(Stop stop){return null;}
    public Long getMaxStopOfRoutes(){return null;}
    public List<Route> getRoutsNotSell(){return null;}
    public List<Route> getTop3RoutesWithMaxRating(){return null;}
    public Service getMostDemandedService(){return null;}
    public List<Service> getServiceNoAddedToPurchases(){return null;}
    public List<TourGuideUser> getTourGuidesWithRating1(){return null;}
}
