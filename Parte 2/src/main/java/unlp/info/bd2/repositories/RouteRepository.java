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
        // Junto las rutas y sus compras
        "{ $lookup: { from: 'purchase', localField: '_id', foreignField: 'route.$id', as: 'purchases' } }",
        // Desarmo las compras, quedandome una combinacion ruta-compra 
        "{ $unwind: '$purchases' }",
        // Me quito la ruta y me quedo con las compras
        "{ $replaceRoot: { newRoot: '$purchases' } }",
        // Projecto la ruta y el rating
        "{ $project: { routeId: '$route', rating: '$review.rating' } }",
        // Agrupo las rutas calculando su rating promedio
        "{ $group: { _id: '$routeId', avgRating: { $avg: '$rating' } } }",
        // Cambio el nombre _id a RouteId
        "{ $project: { routeId: '$_id', avgRating: 1 } }", 
        // Ordeno de Mayor a Menor el Rating
        "{ $sort: { avgRating: -1 } }",
        // Me quedo con los tres mayores
        "{ $limit: 3 }",
        // Me quedo solo con el Id de la ruta
        "{ $project: { routeId: '$_id' } }",
        // Busco las 3 rutas con (logicamente según entiendo) con el mayor promedio de rating
        "{ $lookup: { from: 'route', localField: 'routeId.$id', foreignField: '_id' , as: 'routes' } }",
        // Desarmo las rutas, quedandome una combinacion routeId-route
        "{ $unwind: '$routes' }",
        // Reemplazo los documentos de forma que me quede solo route
        "{ $replaceRoot: { newRoot: '$routes' } }"
    })
    public List<Route> getTop3RoutesWithMaxAverageRating ();
    
    @Aggregation(pipeline = {
        "{ $addFields: { purchaseCount: { $size: '$purchases' } } }",  
        "{ $sort: { purchaseCount: -1 } }",
        "{ $limit: 1 }" 
    })
    public Route getBestSellingRoute();

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
