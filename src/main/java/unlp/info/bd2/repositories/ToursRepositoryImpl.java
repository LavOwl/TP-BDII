package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.PersistenceException;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.User;
import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.utils.ToursException;

public class ToursRepositoryImpl implements ToursRepository {

    @Autowired
    private SessionFactory sessionFactory;

    //IVY
    public User saveOrUpdateUser(User user) throws ToursException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User sameUsernameUser = session.createQuery("FROM User u WHERE u.username = :username", User.class)
                        .setParameter("username", user.getUsername())
                        .uniqueResult();

        if (user.getId() == null || Objects.isNull(session.find(User.class, user.getId()))) {
            if(sameUsernameUser == null){
                session.persist(user);
            }
            else{
                transaction.commit();
                session.close();
                throw new ToursException("Tried to store repeated username");
            }
        } else {
            if(sameUsernameUser == null || user.getId() == sameUsernameUser.getId()){
                session.merge(user);
            }
            else{
                transaction.commit();
                session.close();
                throw new ToursException("Tried to store repeated username");
            }
        }
        transaction.commit();
        session.close();
        return user;
    }

    public Optional<User> getUserById(Long id) { //Removed "throws TourException"; it doesn't make sense
        Session session = sessionFactory.openSession();
        User user = session.find(User.class, id);
        session.close();
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByUsername(String username){ //Removed "throws TourException"; it doesn't make sense
        Session session = sessionFactory.openSession();
        User user = session.createQuery("FROM User u WHERE u.username = :username", User.class)
                       .setParameter("username", username)
                       .uniqueResult();
        return Optional.ofNullable(user);
    }

    public void deleteUser(Long id) throws ToursException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, id);
        if (user != null) {
            if(!user.isActive()){
                throw new ToursException("El usuario se encuentra desactivado");
            }
            try{
                session.remove(user);
            }
            catch(PersistenceException e){
                session.merge(user);
                if(e.getCause() instanceof ToursException){
                    transaction.commit();
                    session.close();
                    throw new ToursException(e.getMessage());
                }
            }
        }
        else{
            throw new ToursException("Tried to delete non-existent user");
        }
        transaction.commit();
        session.close();
    }

    public Stop saveOrUpdateStop(Stop stop){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        if (stop.getId() == null || Objects.isNull(session.find(Stop.class, stop.getId()))) {
            session.persist(stop);
        } else {
            session.merge(stop);
        }
        
        transaction.commit();
        session.close();
        return stop;
    }

    public List<Stop> getStopByNameStart(String name) {
        Session session = sessionFactory.openSession();
        List<Stop> stops = session.createQuery("FROM Stop s WHERE s.name LIKE :name", Stop.class)
                                .setParameter("name", name + "%")
                                .list();
        session.close();
        return stops;
    }
    
    //FABRI
    public Route saveOrUpdateRoute(Route route) throws ToursException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if (route.getId() == null || Objects.isNull(session.find(Route.class, route.getId()))) {
            session.persist(route);
        } else {
            session.merge(route);
        }
        transaction.commit();
        session.close();
        return route;
    }
    
    public Optional<Route> getRouteById(Long id){
        Session session = sessionFactory.openSession();
        Route route = session.find(Route.class, id);
        session.close();
        return Optional.ofNullable(route);
    }
    public List<Route> getRoutesBelowPrice(float price){
        Session session = sessionFactory.openSession();
        List<Route> routes = session.createQuery("FROM Route r WHERE r.price < :price", Route.class)
                                .setParameter("price", price)
                                .list();
        session.close();
        return routes;
     }
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        DriverUser driver = session.createQuery("FROM DriverUser d WHERE d.username = :username", DriverUser.class)
                                .setParameter("username", username)
                                .uniqueResult();
        Route route = session.find(Route.class, idRoute);
        if (driver == null) {
            transaction.commit();
            session.close();
            throw new ToursException("Tried to assign non-existent driver");
        }
        if (route == null) {
            transaction.commit();
            session.close();
            throw new ToursException("Tried to assign driver to non-existent route");
        }
        if (route.getDriverList().contains(driver)) {
            transaction.commit();
            session.close();
            throw new ToursException("Driver already assigned to route");
        }
        route.addDriver(driver);
        driver.addRoute(route);
        //session.merge(driver); // Son necesarios estos merge?
        //session.merge(route); // 
        transaction.commit();
        session.close();
    }
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        TourGuideUser tourGuide = session.createQuery("FROM TourGuideUser t WHERE t.username = :username", TourGuideUser.class)
                                .setParameter("username", username)
                                .uniqueResult();
        Route route = session.find(Route.class, idRoute);
        if (tourGuide == null) {
            transaction.commit();
            session.close();
            throw new ToursException("Tried to assign non-existent tour guide");
        }
        if (route == null) {
            transaction.commit();
            session.close();
            throw new ToursException("Tried to assign tour guide to non-existent route");
        }
        if (route.getTourGuideList().contains(tourGuide)) {
            transaction.commit();
            session.close();
            throw new ToursException("Tour guide already assigned to route");
        }
        route.addTourGuide(tourGuide);
        tourGuide.addRoute(route);
        transaction.commit();
        session.close();
    }
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if ( authorizationNumber == null || businessName == null){
            transaction.commit();
            session.close();
            throw new ToursException("Cannot use null in the name or authorization number of a supplier"); 
        }
        /* 
        Supplier sameAuthorizationNumberSupplier = session.createQuery("FROM Supplier s WHERE s.authorizationNumber = :authorizationNumber", Supplier.class)
                                .setParameter("authorizationNumber", authorizationNumber)
                                .uniqueResult();
        if (sameAuthorizationNumberSupplier != null) {
            transaction.commit();
            session.close();
            throw new ToursException("Tried to store repeated authorization number");
        }
            si el numero de autorizacion no se puede repetir va esto
        */
        Supplier supplier = session.createQuery("INSERT INTO Supplier (businessName, authorizationNumber) VALUES (:businessName, :authorizationNumber)", Supplier.class)
                                .setParameter("businessName", businessName)
                                .setParameter("authorizationNumber", authorizationNumber)
                                .uniqueResult();
        transaction.commit();
        session.close();
        return supplier;
    }

    public Supplier saveOrUpdateSupplier(Supplier supplier) throws ToursException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if (supplier.getId() == null || Objects.isNull(session.find(Supplier.class, supplier.getId()))) {
            session.persist(supplier);
        } else {
            session.merge(supplier);
        }
        transaction.commit();
        session.close();
        return supplier;
    }

    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if (supplier == null) {
            transaction.commit();
            session.close();
            throw new ToursException("Tried to add service to non-existent supplier");
        }
        if (name == null || description == null) {
            transaction.commit();
            session.close();
            throw new ToursException("Cannot use null in the name or description of a service"); 
        }
        Service service = new Service(name, price, description, supplier);

        session.persist(service);
        supplier.addService(service);
        transaction.commit();
        session.close();
        return service;
    }
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Service service = session.find(Service.class, id);
        if (service == null) {
            transaction.commit();
            session.close();
            throw new ToursException("Tried to update non-existent service");
        }
        service.setPrice(newPrice);
        session.merge(service);
        transaction.commit();
        session.close();
        return service;
    }
    
    //FRANCO
    public Optional<Supplier> getSupplierById(Long id) {
        Session session = sessionFactory.openSession();
        Supplier supplier = session.find(Supplier.class, id);
        session.close();
        return Optional.ofNullable(supplier);
    }

    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        Session session = sessionFactory.openSession();
        Supplier supplier = session.createQuery("FROM Supplier s WHERE s.authorizationNumber = :authorizationNumber", Supplier.class)
                       .setParameter("authorizationNumber", authorizationNumber)
                       .uniqueResult();
        session.close();
        return Optional.ofNullable(supplier);
    }
    
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        Session session = sessionFactory.openSession();
        List<Service> services = session.createQuery("FROM Service s WHERE s.name = :serviceName AND s.supplier.id = :supplierId")
                                    .setParameter("serviceName", name)
                                    .setParameter("supplierId", id)
                                    .getResultList();
        session.close();

        if (services.size() > 1) {
            throw new ToursException("There are many (at least 2) services with the same name sent");
        }

        if (services.size() == 1)
            return Optional.of(services.get(0));
        else
            return Optional.empty();
    }
    
    public Purchase savePurchase (Purchase purchase) throws ToursException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(purchase);
        transaction.commit();
        session.close();
        return purchase;
    }
    
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {return null;}
    
    public Optional<Purchase> getPurchaseByCode(String code) {return null;}
    
    public void deletePurchase(Purchase purchase) throws ToursException {}
    
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {return null;}


    //HQL SENTENCES

    //IVY
    public List<Purchase> getAllPurchasesOfUsername(String username){
        Session session = sessionFactory.openSession();
        List<Purchase> purchases = session.createQuery("SELECT p FROM Purchase p JOIN p.user u WHERE u.username = :username", Purchase.class)
                    .setParameter("username", username)
                    .list();
        session.close();
        return purchases;
    }

    public List<User> getUserSpendingMoreThan(float amount){
        Session session = sessionFactory.openSession();
        List<User> users = session.createQuery("SELECT u FROM Purchase p JOIN p.user u GROUP BY u.id HAVING SUM(p.totalPrice) > :amount", User.class)
                    .setParameter("amount", amount)
                    .list();
        session.close();
        return users;
    }
    
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        Session session = sessionFactory.openSession();
        List<Supplier> suppliers = session.createQuery("SELECT s FROM ItemService is JOIN is.service serv JOIN serv.supplier s GROUP BY s.id ORDER BY COUNT(is) DESC", Supplier.class)
                    .setMaxResults(n)
                    .list();
        session.close();
        return suppliers;
    }
    
    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){
        //Criteria isn't clear
        Session session = sessionFactory.openSession();
        List<Purchase> purchases = session.createQuery("SELECT p FROM Purchase p ORDER BY p.totalPrice DESC", Purchase.class)
                    .setMaxResults(10)
                    .list();
        session.close();
        return purchases;
    }

    //FABRI

    public List<User> getTop5UsersMorePurchases(){
        Session session = sessionFactory.openSession();
        List<User> users = session.createQuery("SELECT u FROM Purchase p JOIN p.user u GROUP BY u.id ORDER BY COUNT(p) DESC", User.class)
                    .setMaxResults(5)
                    .list();
        session.close();
        return users;
    };

    public List<Route> getTop3RoutesWithMoreStops(){
        Session session = sessionFactory.openSession();
        List<Route> routes = session.createQuery("SELECT r FROM Route r JOIN r.stops s GROUP BY r.id ORDER BY COUNT(s) DESC", Route.class)
                    .setMaxResults(3)
                    .list();
        session.close();
        return routes;
    };

    public long getCountOfPurchasesBetweenDates (Date start, Date end){
        Session session = sessionFactory.openSession();
        Long count = session.createQuery("SELECT COUNT(p) FROM Purchase p WHERE p.date BETWEEN :start AND :end", Long.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .uniqueResult();
        session.close();
        return count;
    };

    public List<Purchase> getPurchaseWithService(Service service){
        Session session = sessionFactory.openSession();
        List<Purchase> purchases = session.createQuery("SELECT p FROM Purchase p JOIN p.items i WHERE i.service = :service", Purchase.class)
                    .setParameter("service", service)
                    .list();
        session.close();
        return purchases;
    };

    public Long getMaxServicesOfSupplier(){
        Session session = sessionFactory.openSession();
        Long max = session.createQuery("SELECT MAX(s.services.size) FROM Supplier s", Long.class)
                    .uniqueResult();
        session.close();
        return max;
    };

    public List<Route> getRoutsNotSell(){
        Session session = sessionFactory.openSession();
        List<Route> routes = session.createQuery("SELECT r FROM Route r WHERE r NOT IN (SELECT p.route FROM Purchase p)", Route.class)
                    .list();
        session.close();
        return routes;
    };

    public List<Route> getTop3RoutesWithMaxAverageRating(){
        Session session = sessionFactory.openSession();
        List<Route> routes = session.createQuery("SELECT r FROM Route r JOIN r.reviews rev GROUP BY r.id ORDER BY AVG(rev.rating) DESC", Route.class)
                    .setMaxResults(3)
                    .list();
        session.close();
        return routes;
    };

    public List<Route> getRoutesWithStop(Stop stop){
        Session session = sessionFactory.openSession();
        List<Route> routes = session.createQuery("SELECT r FROM Route r JOIN r.stops s WHERE s = :stop", Route.class)
                    .setParameter("stop", stop)
                    .list();
        session.close();
        return routes;
    }

    public Long getMaxStopOfRoutes() {
        Session session = sessionFactory.openSession();
        
        Long max = session.createQuery(
            "SELECT MAX(stopsCount) FROM ( " +
            "    SELECT r.id AS routeId, COUNT(s) AS stopsCount " +
            "    FROM Route r LEFT JOIN r.stops s " +
            "    GROUP BY r.id " +
            ")",
            Long.class
        ).uniqueResult();
        
        session.close();
        return max;
    }
    
    //FRANCO

}
