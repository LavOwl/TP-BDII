package unlp.info.bd2.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

@Repository
public interface RouteRepository extends MongoRepository<Route, ObjectId> {
    public List<Route> findAllByPriceIsLessThan(float price);

    @Aggregation(pipeline = {
        "{ $addFields: { stopsCount: { $size: '$stops' } } }",  
        "{ $sort: { stopsCount: -1 } }",                        
        "{ $limit: ?0 }"                                     
    })
    List<Route> getTopNRoutesWithMoreStops(int n); 

    public List<Route> findByStopsContaining(Stop stop);

    @Aggregation(pipeline = {
        "{ $group: { _id: null, maxStops: { $max: { $size: '$stops' } } } }"
    })
    public Long getMaxStopOfRoutes();

    
}
