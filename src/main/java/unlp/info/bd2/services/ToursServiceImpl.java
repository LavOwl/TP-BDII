package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotNull;
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

    //IVY
    public User createUser(@NotNull String username, @NotNull String password, @NotNull String fullName, @NotNull String email, @NotNull Date birthdate, @NotNull String phoneNumber) throws ToursException{
        User user = new User(username, password, fullName, email, birthdate, phoneNumber);
        return toursRepository.saveOrUpdateUser(user);
    }

    public DriverUser createDriverUser(@NotNull String username, @NotNull String password, @NotNull String fullName, @NotNull String email, @NotNull Date birthdate, @NotNull String phoneNumber, @NotNull String expedient) throws ToursException{
        DriverUser user = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
        return (DriverUser)toursRepository.saveOrUpdateUser(user);
    }

    public TourGuideUser createTourGuideUser(@NotNull String username, @NotNull String password, @NotNull String fullName, @NotNull String email, @NotNull Date birthdate, @NotNull String phoneNumber, @NotNull String education) throws ToursException{
        TourGuideUser user = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
        toursRepository.saveOrUpdateUser(user);
        return (TourGuideUser)toursRepository.saveOrUpdateUser(user);
    }

    public Optional<User> getUserById(Long id) throws ToursException{
        return toursRepository.getUserById(id);
    }

    public Optional<User> getUserByUsername(@NotNull String username) throws ToursException{
        return toursRepository.getUserByUsername(username);
    }

    public User updateUser(@NotNull User user) throws ToursException{
        return toursRepository.saveOrUpdateUser(user);
    }

    public void deleteUser(@NotNull User user) throws ToursException{
        toursRepository.deleteUser(user.getId());
    }

    public Stop createStop(@NotNull String name, @NotNull String description) throws ToursException{
        if(name.contains("_") || name.contains("%")){
            throw new ToursException("Cannot use '% or '_' in the name of a stop"); //Couldn't find an HQL function to avoid SQL injection, could manually map characters to /wildcard or similar, but would take too much time and effort.
        }
        Stop stop = new Stop(name, description);
        return toursRepository.saveOrUpdateStop(stop);
    }

    public List<Stop> getStopByNameStart(@NotNull String name) throws ToursException {
        if(name.contains("_") || name.contains("%")){
            throw new ToursException("Cannot use '% or '_' in the name of a stop"); //Couldn't find an HQL function to avoid SQL injection, could manually map characters to /wildcard or similar, but would take too much time and effort.
        }
        return toursRepository.getStopByNameStart(name);
    }
    
    //FABRI
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{
        Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
        return toursRepository.saveOrUpdateRoute(route);
    }
    public Optional<Route> getRouteById(Long id) {
        return toursRepository.getRouteById(id);
    }
    public List<Route> getRoutesBelowPrice(float price){ // deberia chequear que no me entre como parametro un precio negativo? y que sean numeros?       
        return toursRepository.getRoutesBelowPrice(price);
    }
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException{
        toursRepository.assignDriverByUsername(username, idRoute);
    }
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{
        toursRepository.assignTourGuideByUsername(username, idRoute);
    }
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        if(businessName.contains("_") || businessName.contains("%")){
            throw new ToursException("Cannot use '% or '_' in the name of a supplier"); //Couldn't find an HQL function to avoid SQL injection, could manually map characters to /wildcard or similar, but would take too much time and effort.
        }
        Supplier supplier = new Supplier(businessName, authorizationNumber);
        return toursRepository.saveOrUpdateSupplier(supplier);
    }
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{
        if(name.contains("_") || name.contains("%")){
            throw new ToursException("Cannot use '% or '_' in the name of a service"); //Couldn't find an HQL function to avoid SQL injection, could manually map characters to /wildcard or similar, but would take too much time and effort.
        }
        if(description.contains("_") || description.contains("%")){
            throw new ToursException("Cannot use '% or '_' in the description of a service"); //Couldn't find an HQL function to avoid SQL injection, could manually map characters to /wildcard or similar, but would take too much time and effort.
        }
        return toursRepository.addServiceToSupplier(name, price, description, supplier);
    }
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException{
        return toursRepository.updateServicePriceById(id, newPrice);
    }
    
    //FRANCO
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

    //IVY
    public List<Purchase> getAllPurchasesOfUsername(String username){
        return toursRepository.getAllPurchasesOfUsername(username);
    }

    public List<User> getUserSpendingMoreThan(float mount){
        return toursRepository.getUserSpendingMoreThan(mount);
    }

    public List<Supplier> getTopNSuppliersInPurchases(int n){
        return toursRepository.getTopNSuppliersInPurchases(n);
    }

    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){
        return toursRepository.getTop10MoreExpensivePurchasesInServices();
    }
    
    //FABRI
    public List<User> getTop5UsersMorePurchases(){return null;}
    public long getCountOfPurchasesBetweenDates(Date start, Date end){return 0;}
    public List<Route> getRoutesWithStop(Stop stop){return null;}
    public Long getMaxStopOfRoutes(){return null;}
    public List<Route> getRoutsNotSell(){return null;}
    
    //FRANCO
    public List<Route> getTop3RoutesWithMaxRating(){return null;}
    public Service getMostDemandedService(){return null;}
    public List<Service> getServiceNoAddedToPurchases(){return null;}
    public List<TourGuideUser> getTourGuidesWithRating1(){return null;}
}
