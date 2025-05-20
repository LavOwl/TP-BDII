package unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

public interface RouteRepository extends CrudRepository<Route, Long> {
    public List<Route> findByPriceLessThan(float price);
    public List<Route> findDistinctByPurchasesReviewRating(int rating);
    public List<Route> findByPurchasesIsEmpty();
    public List<Route> getByStopsContaining(Stop stop);

    @Query("SELECT r FROM Route r JOIN r.stops s GROUP BY r.id ORDER BY COUNT(s.id) DESC") //Can't handle aggregations with QueryMethods (Order By Count)
    public List<Route> getTop3RoutesWithMoreStops(Pageable pageable);

    @Query("SELECT r FROM Route r JOIN r.purchases p JOIN p.review rev GROUP BY r.id ORDER BY AVG(rev.rating) DESC") //Can't handle aggregations with QueryMethods (Order By Avg)
    public List<Route> getTop3RoutesWithMaxAverageRating (Pageable pageable);

    @Query("""
            SELECT purchase.route
            FROM Purchase purchase
            GROUP BY purchase.route
            ORDER BY COUNT(purchase) DESC
            """) //Can't handle aggregations with QueryMethods (Order By Count)
    public List<Route> getMostBestSellingRoutes(Pageable pageable);

    @Query("""
        SELECT MAX(size(r.stops)) 
        FROM Route r
        """) //Can't handle aggregations with QueryMethods(max, size of)
    public Long getMaxStopOfRoutes();
}
