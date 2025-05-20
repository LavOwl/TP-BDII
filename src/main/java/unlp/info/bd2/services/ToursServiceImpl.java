package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import unlp.info.bd2.repositories.ItemServiceRepository;
import unlp.info.bd2.repositories.PurchaseRepository;
import unlp.info.bd2.repositories.ReviewRepository;
import unlp.info.bd2.repositories.RouteRepository;
import unlp.info.bd2.repositories.ServiceRepository;
import unlp.info.bd2.repositories.StopRepository;
import unlp.info.bd2.repositories.SupplierRepository;
import unlp.info.bd2.repositories.UserRepository;


import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    ItemServiceRepository itemServiceRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    StopRepository stopRepository;

    @Autowired
    ServiceRepository serviceRepository;
    
    private User uploadUser(User user) throws ToursException{
        try{
            return userRepository.save(user);
        }
        catch(Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException{
        return this.uploadUser(new User(username, password, fullName, email, birthdate, phoneNumber));
    }

    @Override
    @Transactional
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException{
        return (DriverUser) this.uploadUser(new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient));

    }

    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException{
        return (TourGuideUser) this.uploadUser(new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException{
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) throws ToursException{
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public User updateUser(User user) throws ToursException{
        return this.uploadUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException{
        try{
            userRepository.delete(user);
        }
        catch(InvalidDataAccessApiUsageException e){
            userRepository.save(user);
            if(e.getCause().getMessage() != null){
                throw new ToursException(e.getCause().getMessage());
            }
        }
    }
    
    @Override
    @Transactional
    public Stop createStop(String name, String description) throws ToursException{
        try{
            return stopRepository.save(new Stop(name, description));
        }
        catch(Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name){
        return stopRepository.findByNameStartingWith(name);
    }

    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{
        try{
            return routeRepository.save(new Route(name, price, totalKm, maxNumberOfUsers, stops));
        }
        catch (Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id){
        return routeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price){
        return routeRepository.findByPriceLessThan(price);
    }

    @Override
    @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException{
        Optional<DriverUser> optionalUser = userRepository.findDriverUserByUsername(username);
        if (!optionalUser.isPresent()){
            throw new ToursException("User not found");
        }
        DriverUser user = optionalUser.get();
        Optional<Route> optionalRoute = routeRepository.findById(idRoute);
        if (!optionalRoute.isPresent()){
            throw new ToursException("Route not found");
        }
        
        user.addRoute(optionalRoute.get());
    }

    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{
        Optional<TourGuideUser> optionalUser = userRepository.findTourGuideUserByUsername(username);
        if (!optionalUser.isPresent()){
            throw new ToursException("User not found");
        }
        TourGuideUser user = optionalUser.get();

        Optional<Route> optionalRoute = routeRepository.findById(idRoute);
        if (!optionalRoute.isPresent()){
            throw new ToursException("Route not found");
        }
        
        user.addRoute(optionalRoute.get());
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        try{
            return supplierRepository.save(new Supplier(businessName, authorizationNumber));
        }
        catch(Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{
        return serviceRepository.save(new Service(name, price, description, supplier));
    }

    @Override
    @Transactional
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException{
        Optional<Service> optionalService = serviceRepository.findById(id);
        if(optionalService.isPresent()){
            Service service = optionalService.get();
            service.setPrice(newPrice);
            return serviceRepository.save(service);
        }
        else{
            throw new ToursException("Tried to update non-existent service");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id){
        return supplierRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber){
        return supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException{
        return serviceRepository.findByNameAndSupplierId(name, id);
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Route route, User user) throws ToursException{
        return purchaseRepository.save(new Purchase(code, new Date(), route, user));
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException{
        return purchaseRepository.save(new Purchase(code, date, route, user));
    }

    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException{
        return itemServiceRepository.save(new ItemService(service, quantity, purchase));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code){
        return purchaseRepository.findByCode(code);
    }

    @Override
    @Transactional
    public void deletePurchase(Purchase purchase) throws ToursException{
        purchaseRepository.delete(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException{
        return reviewRepository.save(new Review(rating, comment, purchase));
    }

    // CONSULTAS HQL
    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username){
        return purchaseRepository.findByUserUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount){
        return userRepository.findDistinctByPurchaseListTotalPriceGreaterThanEqual(mount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithNumberOfPurchases(int number){
        return userRepository.getUsersWithNumberOfPurchases(number);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        return supplierRepository.getTopNSuppliersInPurchases(PageRequest.of(0, n));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersItemsSold(int n){
        return supplierRepository.getTopNSuppliersItemsSold(PageRequest.of(0, n));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getTop10MoreExpensivePurchasesWithServices(){
        return purchaseRepository.findTop10ByItemServiceListIsNotEmptyOrderByTotalPriceDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMoreStops(){
        return routeRepository.getTop3RoutesWithMoreStops(PageRequest.of(0, 3));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getTop5UsersMorePurchases(){
        return userRepository.getTop5UsersMorePurchases(PageRequest.of(0, 5));
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountOfPurchasesBetweenDates(Date start, Date end){
        return purchaseRepository.countAllByDateIsBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop){
        return routeRepository.getByStopsContaining(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getPurchaseWithService(Service service){
        return purchaseRepository.findByItemServiceListService(service);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes(){
        return routeRepository.getMaxStopOfRoutes();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxServicesOfSupplier(){
        return supplierRepository.getMaxServicesOfSupplier();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell(){
        return routeRepository.findByPurchasesIsEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxAverageRating () {
        return routeRepository.getTop3RoutesWithMaxAverageRating(PageRequest.of(0, 3));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithMinRating () {
        return routeRepository.findDistinctByPurchasesReviewRating(1);
    }

    @Override
    @Transactional(readOnly = true)
    public Service getMostDemandedService () {
        List<Service> servicesMostDemanded = serviceRepository.getMostDemandedServices(PageRequest.of(0, 1));
        return servicesMostDemanded.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Route getMostBestSellingRoute () {
        List<Route> mostBestSellingRoutes = routeRepository.getMostBestSellingRoutes(PageRequest.of(0, 1));
        return mostBestSellingRoutes.get(0);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Service> getServiceNoAddedToPurchases () {
        return serviceRepository.findByItemServiceListIsEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1 () {
        return userRepository.findDistinctByRoutesPurchasesReviewRating(1);
    }

    @Override
    @Transactional(readOnly = true)
    public DriverUser getDriverUserWithMoreRoutes () {
        List<DriverUser> driverUsersWithMoreRoutes = userRepository.getDriverUsersWithMoreRoutes(PageRequest.of(0, 1));
        return driverUsersWithMoreRoutes.get(0);
    }

}
