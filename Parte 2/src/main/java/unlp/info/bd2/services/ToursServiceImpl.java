package unlp.info.bd2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.dao.DuplicateKeyException;

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
import unlp.info.bd2.repositories.PurchaseRepository;
import unlp.info.bd2.repositories.RouteRepository;
import unlp.info.bd2.repositories.ServiceRepository;
import unlp.info.bd2.repositories.StopRepository;
import unlp.info.bd2.repositories.SupplierRepository;
import unlp.info.bd2.repositories.UserRepository;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    private User saveUser(User user) throws ToursException{
        try{
            return userRepository.save(user);
        }
        catch(DuplicateKeyException e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException{
        return this.saveUser(new User(username, password, fullName, email, birthdate, phoneNumber));
    }
    
    @Override
    @Transactional
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException{
        return (DriverUser)this.saveUser(new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient));
    }
    
    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException{
        return (TourGuideUser)this.saveUser(new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(ObjectId id) throws ToursException{
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
        return this.saveUser(user);
    }
    
    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException{
        user = userRepository.findById(user.getId()).orElseThrow(() -> new ToursException("Usuario no existente"));
        try{
            user.logicalRemoval();
            this.userRepository.delete(user);
        }
        catch(RuntimeException e){
            this.userRepository.save(user);
        }
        catch(ToursException e){
            throw e;
        }
    }
    
    @Override
    @Transactional
    public Stop createStop(String name, String description) throws ToursException{
        return stopRepository.save(new Stop(name, description));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name){
        return stopRepository.findByNameStartingWith(name);
    }
    
    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{
        return routeRepository.save(new Route(name, price, totalKm, maxNumberOfUsers, stops));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(ObjectId id){
        return routeRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price){
        return routeRepository.findAllByPriceIsLessThan(price);
    }
    
    @Override
    @Transactional
    public void assignDriverByUsername(String username, ObjectId idRoute) throws ToursException{
        Route route = routeRepository.findById(idRoute).orElseThrow(() -> new ToursException("Id inválido para una ruta"));
        DriverUser driver = userRepository.findDriverUserByUsername(username).orElseThrow(() -> new ToursException("Id inválido para un usuario"));
        route.addDriver(driver);
        routeRepository.save(route);
        userRepository.save(driver);
    }
    
    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, ObjectId idRoute) throws ToursException{
        Route route = routeRepository.findById(idRoute).orElseThrow(() -> new ToursException("Id inválido para una ruta"));
        TourGuideUser tourGuide = userRepository.findTourGuideUserByUsername(username).orElseThrow(() -> new ToursException("Id inválido para un usuario"));
        route.addTourGuide(tourGuide);
        routeRepository.save(route);
        userRepository.save(tourGuide);
    }
    
    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        try{
            return supplierRepository.save(new Supplier(businessName, authorizationNumber));
        }
        catch(DuplicateKeyException e){
            throw new ToursException("Código de autorización duplicado");
        }
    }
    
    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{
        Service service = serviceRepository.save(new Service(name, price, description, supplier));
        supplierRepository.save(supplier);
        return service;
    }
    
    @Override
    @Transactional
    public Service updateServicePriceById(ObjectId id, float newPrice) throws ToursException{
        return serviceRepository.save(serviceRepository.findById(id).orElseThrow(() -> new ToursException("Id inexistente para un servicio")).updatePrice(newPrice));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(ObjectId id){
        return supplierRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber){
        return supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, ObjectId id) throws ToursException{
        return serviceRepository.findByNameAndSupplierId(name, id);
    }
    
    private Purchase savePurchase(Purchase purchase) throws ToursException{
        try{
            purchase = purchaseRepository.save(purchase);
            userRepository.save(purchase.getUser());
            //routeRepository.save(purchase.getRoute());
            return purchase;
        }
        catch(DuplicateKeyException e){
            throw new ToursException("Constraint Error");
        }
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Route route, User user) throws ToursException{
        return this.savePurchase(new Purchase(code, new Date(), route, user));
    }
    
    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException{
        return this.savePurchase(new Purchase(code, date, route, user));
    }
    
    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException{
        ItemService itemService = new ItemService(quantity, service, purchase);
        purchaseRepository.save(purchase);
        serviceRepository.save(service);
        return itemService;
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
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException{
        purchase.setReview(new Review(rating, comment, purchase));
        return purchaseRepository.save(purchase).getReview();
    }

    // CONSULTAS HQL
    
    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            return optionalUser.get().getPurchaseList();
        }
        return new ArrayList<Purchase>();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount){
        return purchaseRepository.getUsersOfPurchasesOver(mount);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithNumberOfPurchases(int number){
        return userRepository.getUsersWithNPurchases(number);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        return purchaseRepository.getTopNMostPresentSuppliers(n);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersItemsSold(int n){
        return serviceRepository.getTopNSuppliersInItemsSold(n);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getTop5UsersMorePurchases(){
        return userRepository.getTopNUsersMorePurchases(5);
    }
    
    //Fabri

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMoreStops(){return null;}
    
    @Override
    @Transactional(readOnly = true)
    public Long getCountOfPurchasesBetweenDates(Date start, Date end){return null;}
    
    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop){return null;}
    
    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes(){return null;}
    
    @Override
    @Transactional(readOnly = true)
    public Long getMaxServicesOfSupplier(){return null;}
    
    //Franco

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxAverageRating () {
        return this.purchaseRepository.getTop3RoutesWithMaxAverageRating();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithMinRating () {
        return this.routeRepository.getRoutesWithMinRating();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Service getMostDemandedService () {
        return this.serviceRepository.getMostDemandedService();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Route getMostBestSellingRoute () {
        return this.routeRepository.getMostBestSellingRoute();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Service> getServiceNoAddedToPurchases () {
        return null; // this.serviceRepository.getServiceNoAddedToPurchases(); // Nose como carajos haria un not exist acá
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1 () {
        return userRepository.getTourGuidesWithRating1();
    }
    
    @Override
    @Transactional(readOnly = true)
    public DriverUser getDriverUserWithMoreRoutes () {
        return userRepository.getDriverUserWithMoreRoutes();
    }
}
