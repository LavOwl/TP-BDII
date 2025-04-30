package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

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

    private User saveUser(User user) throws ToursException{
        try{
            return toursRepository.save(user);
        }
        catch (ConstraintViolationException c){
            throw new ToursException("Constraint Violation");
        }
    }

    //IVY
    @Override
    @Transactional
    public User createUser(@NotNull String username, @NotNull String password, @NotNull String fullName, @NotNull String email, @NotNull Date birthdate, @NotNull String phoneNumber) throws ToursException{
        return saveUser(new User(username, password, fullName, email, birthdate, phoneNumber));
    }

    @Override
    @Transactional
    public DriverUser createDriverUser(@NotNull String username, @NotNull String password, @NotNull String fullName, @NotNull String email, @NotNull Date birthdate, @NotNull String phoneNumber, @NotNull String expedient) throws ToursException{
        return (DriverUser) saveUser(new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient));
    }

    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(@NotNull String username, @NotNull String password, @NotNull String fullName, @NotNull String email, @NotNull Date birthdate, @NotNull String phoneNumber, @NotNull String education) throws ToursException{
        return (TourGuideUser) saveUser(new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException{
        return toursRepository.getById(User.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(@NotNull String username) throws ToursException{
        return toursRepository.getUserByUsername(username);
    }

    @Override
    @Transactional
    public User updateUser(@NotNull User user) throws ToursException{
        return toursRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(@NotNull User user) throws ToursException{
        try{
            toursRepository.delete(user);
        }
        catch(IllegalStateException e){
            if("El usuario se encuentra desactivado".equals(e.getMessage()) || "El usuario no puede ser desactivado".equals(e.getMessage())){
                throw new ToursException(e.getMessage());
            }
            else{
                toursRepository.save(user);
            }
        }
    }

    @Override
    @Transactional
    public Stop createStop(@NotNull String name, @NotNull String description) throws ToursException{
        if(name.contains("_") || name.contains("%")){
            throw new ToursException("Cannot use '% or '_' in the name of a stop"); //Couldn't find an HQL function to avoid SQL injection, could manually map characters to /wildcard or similar, but would take too much time and effort.
        }
        return toursRepository.save(new Stop(name, description));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(@NotNull String name) throws ToursException {
        if(name.contains("_") || name.contains("%")){
            throw new ToursException("Cannot use '% or '_' in the name of a stop"); //Couldn't find an HQL function to avoid SQL injection, could manually map characters to /wildcard or similar, but would take too much time and effort.
        }
        return toursRepository.getStopByNameStart(name);
    }
    
    //FABRI
    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{
        return toursRepository.save(new Route(name, price, totalKm, maxNumberOfUsers, stops));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return toursRepository.getById(Route.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price){ // deberia chequear que no me entre como parametro un precio negativo? y que sean numeros?       
        return toursRepository.getRoutesBelowPrice(price);
    }

    @Override
    @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException{
        Optional<User> optionalDriverUser = toursRepository.getUserByUsername(username);
        Optional<Route> optionalRoute = toursRepository.getById(Route.class, idRoute);
        if(!optionalDriverUser.isPresent() || !optionalRoute.isPresent()){
            throw new ToursException("Invalid username or id");
        }
        DriverUser driverUser = (DriverUser) optionalDriverUser.get();
        driverUser.addRoute(optionalRoute.get());
        toursRepository.save(driverUser);
    }
    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{
        Optional<User> optionalTourGuideUser = toursRepository.getUserByUsername(username);
        Optional<Route> optionalRoute = toursRepository.getById(Route.class, idRoute);
        if(!optionalTourGuideUser.isPresent() || !optionalRoute.isPresent()){
            throw new ToursException("Invalid username or id");
        }
        TourGuideUser tourGuideUser = (TourGuideUser) optionalTourGuideUser.get();
        tourGuideUser.addRoute(optionalRoute.get());
        toursRepository.save(tourGuideUser);
    }

    private void checkString(String s) throws ToursException{
        if(s.contains("_") || s.contains("%"))
            throw new ToursException("Cannot use '% or '_' in the name of a supplier");
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        checkString(businessName);
        try{
            return toursRepository.save(new Supplier(businessName, authorizationNumber));
        }
        catch(ConstraintViolationException c){
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{
        checkString(name);
        checkString(description);
        return toursRepository.save(new Service(name, price, description, supplier));
    }

    @Override
    @Transactional
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException{
        Optional<Service> optionalService = toursRepository.getById(Service.class, id);
        if(!optionalService.isPresent()){
            throw new ToursException("Service does not exist");
        }
        Service service = optionalService.get();
        service.setPrice(newPrice);
        return toursRepository.save(service);
    }
    
     //FRANCO
    @Override
    @Transactional(readOnly = true)
     public Optional<Supplier> getSupplierById (Long id) {
        return toursRepository.getById(Supplier.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber (String authorizationNumber) {
        return toursRepository.getSupplierByAuthorizationNumber(authorizationNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId (String name, Long id) throws ToursException {
        return toursRepository.getServiceByNameAndSupplierId(name, id);
    }

    private Purchase savePurchase (Purchase purchase) throws ToursException {
        try{
            return toursRepository.save(purchase);
        }
        catch (ConstraintViolationException c){
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    @Transactional
    public Purchase createPurchase (String code, Route route, User user) throws ToursException {
        return savePurchase(new Purchase(code, new Date(), route, user));
    }

    @Override
    @Transactional
    public Purchase createPurchase (String code, Date date, Route route, User user) throws ToursException {
        return savePurchase(new Purchase(code, date, route, user));
    }

    @Override
    @Transactional
    public ItemService addItemToPurchase (Service service, int quantity, Purchase purchase) throws ToursException {
        return toursRepository.save(new ItemService(service, quantity, purchase));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode (String code) {
        return toursRepository.getPurchaseByCode(code);
    }

    @Override
    @Transactional
    public void deletePurchase (Purchase purchase) throws ToursException {
        toursRepository.delete(purchase);
    }

    @Override
    @Transactional
    public Review addReviewToPurchase (int rating, String comment, Purchase purchase) throws ToursException {
        //supongo que el rating debe estar entre 0 y 10
        if (rating < 0 && rating > 10)
            throw new ToursException("The rating of the review must be between 0 and 10");
            
        return toursRepository.save(new Review(rating, comment, purchase));
    }

    // CONSULTAS HQL

    //IVY
    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username){
        return toursRepository.getAllPurchasesOfUsername(username);
    }
    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount){
        return toursRepository.getUserSpendingMoreThan(mount);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        return toursRepository.getTopNSuppliersInPurchases(n);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){
        return toursRepository.getTop10MoreExpensivePurchasesInServices();
    }
    
    //FABRI
    @Override
    @Transactional(readOnly = true)
    public List<User> getTop5UsersMorePurchases(){
        return toursRepository.getTop5UsersMorePurchases();
    }
    @Override
    @Transactional(readOnly = true)
    public long getCountOfPurchasesBetweenDates(Date start, Date end){
        return toursRepository.getCountOfPurchasesBetweenDates(start, end);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop){
        return toursRepository.getRoutesWithStop(stop);
    }
    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes(){
        return toursRepository.getMaxStopOfRoutes();
    }
    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell(){
        return toursRepository.getRoutsNotSell();
    }
    
    //FRANCO
    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxRating() {
        return toursRepository.getTop3RoutesWithMaxAverageRating(); // Usa el que hizo Fabri
    }
    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithMinRating() {
        return toursRepository.getRoutesWithMinRating();
    }
    @Override
    @Transactional(readOnly = true)
    public Service getMostDemandedService() {
        return toursRepository.getMostDemandedService();
    }
    @Override
    @Transactional(readOnly = true)
    public List<Service> getServiceNoAddedToPurchases() {
        return toursRepository.getServiceNoAddedToPurchases();
    }
    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return toursRepository.getTourGuidesWithRating1();
    }
}
