package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    //IVY

    public <T> T save(T persistableObject) throws ConstraintViolationException{
        Session session = sessionFactory.getCurrentSession();
        return session.merge(persistableObject);
    }

    public <T> void delete(T persistableObject){
        Session session = sessionFactory.getCurrentSession();
        session.remove(persistableObject);
    }

    public <T> Optional<T> getById(Class<T> clazz, Long id){
        Session session = sessionFactory.getCurrentSession();
        T persistableObject = session.find(clazz, id);
        return Optional.ofNullable(persistableObject);
    }

    public Optional<User> getUserByUsername(String username){ //Removed "throws TourException"; it doesn't make sense
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("FROM User u WHERE u.username = :username", User.class)
                       .setParameter("username", username)
                       .uniqueResult();
        return Optional.ofNullable(user);
    }

    public List<Stop> getStopByNameStart(String name) {
        Session session = sessionFactory.getCurrentSession();
        List<Stop> stops = session.createQuery("FROM Stop s WHERE s.name LIKE :name", Stop.class)
                                .setParameter("name", name + "%")
                                .list();
        return stops;
    }
    
    //FABRI

    public List<Route> getRoutesBelowPrice(float price){
        Session session = sessionFactory.getCurrentSession();
        List<Route> routes = session.createQuery("FROM Route r WHERE r.price < :price", Route.class)
                                .setParameter("price", price)
                                .list();
        
        return routes;
     }

     
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        DriverUser driver = session.createQuery("FROM DriverUser d WHERE d.username = :username", DriverUser.class)
                                .setParameter("username", username)
                                .uniqueResult();
        Route route = session.find(Route.class, idRoute);
        if (driver == null) {
            
            throw new ToursException("Tried to assign non-existent driver"); //Seems OK here
        }
        if (route == null) {
            
            throw new ToursException("Tried to assign driver to non-existent route"); //Seems OK here
        }
        if (route.getDriverList().contains(driver)) {
            
            throw new ToursException("Driver already assigned to route"); //Seems OK here
        }
        driver.addRoute(route);

        session.merge(driver);        
    }

    
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        TourGuideUser tourGuide = session.createQuery("FROM TourGuideUser t WHERE t.username = :username", TourGuideUser.class)
                                .setParameter("username", username)
                                .uniqueResult();
        Route route = session.find(Route.class, idRoute);
        if (tourGuide == null) {
            
            throw new ToursException("Tried to assign non-existent tour guide"); //Seems OK here
        }
        if (route == null) {
            
            throw new ToursException("Tried to assign tour guide to non-existent route"); //Seems OK here
        }
        if (route.getTourGuideList().contains(tourGuide)) {
            
            throw new ToursException("Tour guide already assigned to route"); //Seems OK here
        }
        route.addTourGuide(tourGuide);
        tourGuide.addRoute(route);
        
    }

    public Service updateServicePriceById(Long id, float newPrice) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        
        Service service = session.find(Service.class, id);
        if (service == null) {
            
            
            throw new ToursException("Tried to update non-existent service"); //Seems OK here
        }
        service.setPrice(newPrice);
        session.merge(service);
        
        
        return service;
    }
    
    //FRANCO

    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.createQuery("FROM Supplier s WHERE s.authorizationNumber = :authorizationNumber", Supplier.class)
                            .setParameter("authorizationNumber", authorizationNumber)
                            .uniqueResult();

        return Optional.ofNullable(supplier);
    }
    
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        List<Service> services = session.createQuery("FROM Service s WHERE s.name = :serviceName AND s.supplier.id = :supplierId", Service.class)
                                    .setParameter("serviceName", name)
                                    .setParameter("supplierId", id)
                                    .getResultList();

        if (services.size() > 1) {
            throw new ToursException("There are many (at least 2) services with the same name sent"); //This a rule? If so, Seems OK here
        }

        if (services.size() == 1)
            return Optional.of(services.get(0));
        else
            return Optional.empty();
    }
    
    public Optional<Purchase> getPurchaseByCode(String code) {
        Session session = sessionFactory.getCurrentSession();
        Purchase purchase = session.createQuery("FROM Purchase p WHERE p.code = :purchaseCode", Purchase.class)
                            .setParameter("purchaseCode", code)
                            .uniqueResult();
                            
        return Optional.ofNullable(purchase);
    }
    
    
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
        List<Supplier> suppliers = session.createQuery("SELECT s FROM ItemService is JOIN is.service serv JOIN serv.supplier s JOIN is.purchase p GROUP BY s.id ORDER BY COUNT(p) DESC", Supplier.class)
                    .setMaxResults(n)
                    .list();
        
        return suppliers;
    }
    
    public List<Purchase> getTop10MoreExpensivePurchasesInServices() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("""
        SELECT p FROM Purchase p
        WHERE EXISTS (SELECT 1 FROM ItemService i WHERE i.purchase = p)
        ORDER BY p.totalPrice DESC
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
}
