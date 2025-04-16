package unlp.info.bd2.repositories;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
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

    private static final Logger log = LoggerFactory.getLogger(ToursRepositoryImpl.class);

    //IVY
    @Transactional
    public User saveOrUpdateUser(User user) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        User sameId = null;
        if(!(user.getId() == null))
            sameId = session.find(User.class, user.getId());

        if (user.getId() == null || Objects.isNull(sameId)) {
            User sameUsernameUser = session.createQuery("FROM User u WHERE u.username = :username", User.class)
                        .setParameter("username", user.getUsername())
                        .uniqueResult();
            if(sameUsernameUser == null){
                session.persist(user);
            }
            else{
                throw new ToursException("Tried to store repeated username");
            }
        } else {

            session.merge(user);
        }
        

        return user;
    }

    public Optional<User> getUserById(Long id) { //Removed "throws TourException"; it doesn't make sense
        Session session = sessionFactory.getCurrentSession();
        User user = session.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByUsername(String username){ //Removed "throws TourException"; it doesn't make sense
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("FROM User u WHERE u.username = :username", User.class)
                       .setParameter("username", username)
                       .uniqueResult();
        return Optional.ofNullable(user);
    }

    /*
    @Transactional
    public void deleteUser(Long id) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
    
        if (user == null) {
            throw new ToursException("Tried to delete non-existent user");
        }
    
        if (!user.isActive()) {
            throw new ToursException("El usuario se encuentra desactivado");
        }
    
        if (user instanceof TourGuideUser) {
            throw new ToursException("El usuario no puede ser desactivado");
        }
    
        if (user.getPurchaseList() != null && !user.getPurchaseList().isEmpty()) {
            // Tiene compras: solo desactivar
            user.desactivar();
            session.merge(user);
        } else {
            // No tiene compras: eliminar físicamente
            session.remove(user);
        }
    }*/

    @Transactional
    public void deleteUser(Long id) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if (user != null) {
            try{
                session.remove(user);
            }
            catch(IllegalStateException e){
                session.merge(user);
                if(e.getMessage() == "El usuario se encuentra desactivado" || e.getMessage() == "El usuario no puede ser desactivado"){
                    throw new ToursException(e.getMessage());
                }
            }
        }
        else{
            throw new ToursException("Tried to delete non-existent user");
        }
    }

    @Transactional
    public Stop saveOrUpdateStop(Stop stop){
        Session session = sessionFactory.getCurrentSession();

        if (stop.getId() == null || Objects.isNull(session.find(Stop.class, stop.getId()))) {
            session.persist(stop);
        } else {
            session.merge(stop);
        }
        
        
        return stop;
    }

    public List<Stop> getStopByNameStart(String name) {
        Session session = sessionFactory.getCurrentSession();
        List<Stop> stops = session.createQuery("FROM Stop s WHERE s.name LIKE :name", Stop.class)
                                .setParameter("name", name + "%")
                                .list();
        
        return stops;
    }
    
    //FABRI
    @Transactional
    public Route saveOrUpdateRoute(Route route) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        if (route.getId() == null || Objects.isNull(session.find(Route.class, route.getId()))) {
            session.persist(route);
        } else {
            session.merge(route);
        }
        
        return route;
    }
    
    public Optional<Route> getRouteById(Long id){
        Session session = sessionFactory.getCurrentSession();
        Route route = session.find(Route.class, id);
        
        return Optional.ofNullable(route);
    }

    public List<Route> getRoutesBelowPrice(float price){
        Session session = sessionFactory.getCurrentSession();
        List<Route> routes = session.createQuery("FROM Route r WHERE r.price < :price", Route.class)
                                .setParameter("price", price)
                                .list();
        
        return routes;
     }

     @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        DriverUser driver = session.createQuery("FROM DriverUser d WHERE d.username = :username", DriverUser.class)
                                .setParameter("username", username)
                                .uniqueResult();
        Route route = session.find(Route.class, idRoute);
        if (driver == null) {
            
            throw new ToursException("Tried to assign non-existent driver");
        }
        if (route == null) {
            
            throw new ToursException("Tried to assign driver to non-existent route");
        }
        if (route.getDriverList().contains(driver)) {
            
            throw new ToursException("Driver already assigned to route");
        }
        route.addDriver(driver);
        driver.addRoute(route);
        //session.merge(driver); // Son necesarios estos merge?
        //session.merge(route); // 
        
    }

    @Transactional
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        TourGuideUser tourGuide = session.createQuery("FROM TourGuideUser t WHERE t.username = :username", TourGuideUser.class)
                                .setParameter("username", username)
                                .uniqueResult();
        Route route = session.find(Route.class, idRoute);
        if (tourGuide == null) {
            
            throw new ToursException("Tried to assign non-existent tour guide");
        }
        if (route == null) {
            
            throw new ToursException("Tried to assign tour guide to non-existent route");
        }
        if (route.getTourGuideList().contains(tourGuide)) {
            
            throw new ToursException("Tour guide already assigned to route");
        }
        route.addTourGuide(tourGuide);
        tourGuide.addRoute(route);
        
    }

    @Transactional
    public Supplier saveOrUpdateSupplier(Supplier supplier) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        if (supplier.getId() == null || Objects.isNull(session.find(Supplier.class, supplier.getId()))) {
            Supplier sameAuthorizationNumberSupplier = session.createQuery("FROM Supplier s WHERE s.authorizationNumber = :authorizationNumber", Supplier.class)
                                .setParameter("authorizationNumber", supplier.getAuthorizationNumber())
                                .uniqueResult();
            if (sameAuthorizationNumberSupplier != null) {
            throw new ToursException("Constraint Violation");
        }
            session.persist(supplier);
        } else {
            session.merge(supplier);
        }
        
        return supplier;
    }

    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        if (supplier == null) {
            
            
            throw new ToursException("Tried to add service to non-existent supplier");
        }
        if (name == null || description == null) {
            
            
            throw new ToursException("Cannot use null in the name or description of a service"); 
        }
        Service service = new Service(name, price, description, supplier);

        session.persist(service);
        supplier.addService(service);
        
        
        return service;
    }
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        
        Service service = session.find(Service.class, id);
        if (service == null) {
            
            
            throw new ToursException("Tried to update non-existent service");
        }
        service.setPrice(newPrice);
        session.merge(service);
        
        
        return service;
    }
    
    //FRANCO
    public Optional<Supplier> getSupplierById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.find(Supplier.class, id);

        return Optional.ofNullable(supplier);
    }

    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.createQuery("FROM Supplier s WHERE s.authorizationNumber = :authorizationNumber", Supplier.class)
                            .setParameter("authorizationNumber", authorizationNumber)
                            .uniqueResult();

        return Optional.ofNullable(supplier);
    }
    
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        List<Service> services = session.createQuery("FROM Service s WHERE s.name = :serviceName AND s.supplier.id = :supplierId")
                                    .setParameter("serviceName", name)
                                    .setParameter("supplierId", id)
                                    .getResultList();

        if (services.size() > 1) {
            throw new ToursException("There are many (at least 2) services with the same name sent");
        }

        if (services.size() == 1)
            return Optional.of(services.get(0));
        else
            return Optional.empty();
    }
    
    @Transactional
    public Purchase savePurchase (Purchase purchase) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        session.persist(purchase);
        return purchase;
    }
    
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService item = new ItemService(service, quantity, purchase);
        float price = quantity * service.getPrice();
        purchase.addItem(item, price);
        service.addItemService(item);

        Session session = sessionFactory.getCurrentSession();
        session.persist(item);
        return item;
    }
    
    public Optional<Purchase> getPurchaseByCode(String code) {
        Session session = sessionFactory.getCurrentSession();
        Purchase purchase = session.createQuery("FROM Purchase p WHERE p.code = :purchaseCode", Purchase.class)
                            .setParameter("purchaseCode", code)
                            .uniqueResult();
                            
        return Optional.ofNullable(purchase);
    }
    
    @Transactional
    public void deletePurchase(Purchase purchase) throws ToursException { //deberia funcionar gracias a la anotaciones en las clases
        Session session = sessionFactory.getCurrentSession();
        session.clear(); // esto para consultar ¿por que anda con esto y sin esto no?
        session.remove(purchase);
    }
    
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        Review review = purchase.addReview(rating, comment);
        Session session = sessionFactory.getCurrentSession();
        session.persist(review);
        return review;
    }


    //HQL SENTENCES

    //IVY
    public List<Purchase> getAllPurchasesOfUsername(String username){
        Session session = sessionFactory.getCurrentSession();
        List<Purchase> purchases = session.createQuery("SELECT p FROM Purchase p JOIN p.user u WHERE u.username = :username", Purchase.class)
                    .setParameter("username", username)
                    .list();
        
        return purchases;
    }

    public List<User> getUserSpendingMoreThan(float amount) {
        Session session = sessionFactory.getCurrentSession();
        
        return session.createQuery("""
            SELECT DISTINCT p.user 
            FROM Purchase p
            WHERE (p.route.price + 
                  (SELECT COALESCE(SUM(i.service.price * i.quantity), 0) 
                   FROM ItemService i 
                   WHERE i.purchase = p)) >= :amount
            """, User.class)
            .setParameter("amount", amount)
            .list();
    }
    
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        Session session = sessionFactory.getCurrentSession();
        List<Supplier> suppliers = session.createQuery("SELECT s FROM ItemService is JOIN is.service serv JOIN serv.supplier s GROUP BY s.id ORDER BY COUNT(is) DESC", Supplier.class)
                    .setMaxResults(n)
                    .list();
        
        return suppliers;
    }
    
    public List<Purchase> getTop10MoreExpensivePurchasesInServices() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("""
        SELECT p FROM Purchase p
        WHERE EXISTS (SELECT 1 FROM ItemService i WHERE i.purchase = p)
        ORDER BY (
            p.route.price + 
            (SELECT COALESCE(SUM(i.service.price * i.quantity), 0) 
            FROM ItemService i WHERE i.purchase = p)
        ) DESC
        """, Purchase.class)
        .setMaxResults(10)
        .list();
    }
    

    //FABRI

    public List<User> getTop5UsersMorePurchases(){
        Session session = sessionFactory.getCurrentSession();
        List<User> users = session.createQuery("SELECT u FROM Purchase p JOIN p.user u GROUP BY u.id ORDER BY COUNT(p) DESC", User.class)
                    .setMaxResults(5)
                    .list();
        
        return users;
    };

    public List<Route> getTop3RoutesWithMoreStops(){
        Session session = sessionFactory.getCurrentSession();
        List<Route> routes = session.createQuery("SELECT r FROM Route r JOIN r.stops s GROUP BY r.id ORDER BY COUNT(s) DESC", Route.class)
                    .setMaxResults(3)
                    .list();
        
        return routes;
    };

    public long getCountOfPurchasesBetweenDates (Date start, Date end){
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery("SELECT COUNT(p) FROM Purchase p WHERE p.date BETWEEN :start AND :end", Long.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .uniqueResult();
        
        return count;
    };

    public List<Purchase> getPurchaseWithService(Service service){
        Session session = sessionFactory.getCurrentSession();
        List<Purchase> purchases = session.createQuery("SELECT p FROM Purchase p JOIN p.items i WHERE i.service = :service", Purchase.class)
                    .setParameter("service", service)
                    .list();
        
        return purchases;
    };

    public Long getMaxServicesOfSupplier(){
        Session session = sessionFactory.getCurrentSession();
        Long max = session.createQuery("SELECT MAX(s.services.size) FROM Supplier s", Long.class)
                    .uniqueResult();
        
        return max;
    };

    public List<Route> getRoutsNotSell(){
        Session session = sessionFactory.getCurrentSession();
        List<Route> routes = session.createQuery("SELECT r FROM Route r WHERE r NOT IN (SELECT p.route FROM Purchase p)", Route.class)
                    .list();
        
        return routes;
    };

    public List<Route> getTop3RoutesWithMaxAverageRating(){
        Session session = sessionFactory.getCurrentSession();
        List<Route> routes = session.createQuery("SELECT r FROM Route r JOIN r.purchases p JOIN p.review rev GROUP BY r.id ORDER BY AVG(rev.rating) DESC", Route.class)
                    .setMaxResults(3)
                    .list();
        
        return routes;
    };

    public List<Route> getRoutesWithStop(Stop stop){
        Session session = sessionFactory.getCurrentSession();
        List<Route> routes = session.createQuery("SELECT r FROM Route r JOIN r.stops s WHERE s = :stop", Route.class)
                    .setParameter("stop", stop)
                    .list();
        
        return routes;
    }

    public Long getMaxStopOfRoutes() {
        Session session = sessionFactory.getCurrentSession();
        
        Long max = session.createQuery(
            "SELECT MAX(stopsCount) FROM ( " +
            "    SELECT r.id AS routeId, COUNT(s) AS stopsCount " +
            "    FROM Route r LEFT JOIN r.stops s " +
            "    GROUP BY r.id " +
            ")",
            Long.class
        ).uniqueResult();
        
        
        return max;
    }
    
    //FRANCO
    //public List<Route> getTop3RoutesWithMaxAverageRating(){return null;} 

    public List<Route> getRoutesWithMinRating() {
        Session session = sessionFactory.getCurrentSession();
        List<Route> routes = session.createQuery(
                            "SELECT DISTINCT route " +
                            "FROM Route route " +
                            "WHERE EXISTS ( " +
                                "SELECT 1 "+
                                "FROM Purchase p " +
                                "WHERE p.route = route AND p.review.rating = 1" +
                            ")", Route.class)
                            .list();
        return routes;
    }

    public Service getMostDemandedService() {
        Session session = sessionFactory.getCurrentSession();
        Service service = session.createQuery(
                            "SELECT item.service " +
                            "FROM ItemService item " +
                            "GROUP BY item.service " +
                            "ORDER BY SUM(item.quantity) DESC", Service.class)
                            .setMaxResults(1)
                            .uniqueResult();
        return service;
    }

    public List<Service> getServiceNoAddedToPurchases() {
        Session session = sessionFactory.getCurrentSession();
        List<Service> services = session.createQuery(
                                "SELECT service " + 
                                "FROM Service service " +
                                "WHERE NOT EXISTS (" +
                                    "SELECT 1 " +
                                    "FROM ItemService item " +
                                    "WHERE item.service = service" +
                                ")", Service.class)
                                .list();
        return services;
    }

    public List<TourGuideUser> getTourGuidesWithRating1() {
        Session session = sessionFactory.getCurrentSession();
        List<TourGuideUser> tgusers = session.createQuery(
                                        "SELECT DISTINCT tguser " +
                                        "FROM TourGuideUser tguser " +
                                                "JOIN tguser.routes route JOIN route.purchases purchase " +
                                                "JOIN purchase.review review " + 
                                        "WHERE review.rating = 1", TourGuideUser.class)
                                        .list();
        return tgusers;
    }
    /* Resguardo de la consulta con IN (decidí cambiarla por la eficiencia)
        List<TourGuideUser> tgusers = session.createQuery(
                                        "SELECT DISTINCT tguser " +
                                        "FROM TourGuideUser tguser JOIN tguser.routes route " + 
                                        "WHERE route IN (" +
                                            "SELECT route " + 
                                            "FROM Route route JOIN route.purchases purchase JOIN purchase.review review " + 
                                            "WHERE review.rating = 1" +
                                        ")", TourGuideUser.class)
                                        .list();
     */
}
