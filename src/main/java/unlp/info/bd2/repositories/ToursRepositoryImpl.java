package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.User;
import unlp.info.bd2.utils.ToursException;

public class ToursRepositoryImpl implements ToursRepository {

    @Autowired
    private SessionFactory sessionFactory;

    //IVY
    public void saveOrUpdateUser(User user) throws ToursException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User sameUsernameUser = session.createQuery("FROM User u WHERE u.username = :username", User.class)
                        .setParameter("username", user.getUsername())
                        .uniqueResult();

        if (Objects.isNull(session.find(User.class, user.getId()))) {
            if(sameUsernameUser == null){
                session.persist(user);
            }
            else{
                throw new ToursException("Tried to store repeated username");
            }
        } else {
            if(sameUsernameUser == null || user.getId() == sameUsernameUser.getId()){
                session.merge(user);
            }
            else{
                throw new ToursException("Tried to store repeated username");
            }
        }
        transaction.commit();
        session.close();
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
            session.remove(user);
        }
        else{
            throw new ToursException("Tried to delete non-existent user");
        }
        transaction.commit();
        session.close();
    }

    public void saveOrUpdateStop(Stop stop){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        if (Objects.isNull(session.find(Stop.class, stop.getId()))) {
            session.persist(stop);
        } else {
            session.merge(stop);
        }
        
        transaction.commit();
        session.close();
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
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{return null;}
    public Optional<Route> getRouteById(Long id){return null;}
    public List<Route> getRoutesBelowPrice(float price){return null;}
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException{}
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{}
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{return null;}
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{return null;}
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException{return null;}
    
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
}
