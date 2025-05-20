package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findByUsername(String username);
    public Optional<TourGuideUser> findTourGuideUserByUsername(String username);
    public Optional<DriverUser> findDriverUserByUsername(String username);
    public List<TourGuideUser> findDistinctByRoutesPurchasesReviewRating(int rating);
    public List<User> findDistinctByPurchaseListTotalPriceGreaterThanEqual(float mount);

    @Query("SELECT u FROM Purchase p JOIN p.user u GROUP BY u.id HAVING COUNT(p.id) >= :number") //Can't handle aggregations with QueryMethods(Having Count)
    public List<User> getUsersWithNumberOfPurchases(int number);

    @Query("""
            SELECT DISTINCT dUser
            FROM DriverUser dUser
            GROUP BY dUser
            ORDER BY size(dUser.routes) DESC
            """) //Can't handle aggregations with QueryMethods(Order By Size)
    public List<DriverUser> getDriverUsersWithMoreRoutes (Pageable pageable);

    @Query("SELECT u FROM Purchase p JOIN p.user u GROUP BY u.id ORDER BY COUNT(p) DESC") //Can't handle aggregations with QueryMethods(Order By Count)
    public List<User> getTop5UsersMorePurchases (Pageable pageable);
}
