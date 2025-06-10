package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Purchase;
import java.util.Date;
import unlp.info.bd2.model.Route;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {
    public Optional<Purchase> findByCode(String code);
    public Long countByDateBetween(Date start, Date end);

    @Aggregation(pipeline = {
        //Este la verdad tampoco se que falla, seguro el lookup por el tema de ObjectID
        "{ $match: { 'review.rating': { $exists: true } } }",
        "{ $project: { routeId: '$route.$id', rating: '$review.rating' } }",
        "{ $group: { _id: '$routeId', averageRating: { $avg: '$rating' } } }",
        "{ $sort: { averageRating: -1 } }",
        "{ $limit: 3 }",
        "{ $lookup: { from: 'route', localField: '_id', foreignField: '_id', as: 'routeDetails' } }",
        "{ $unwind: '$routeDetails' }",
        "{ $replaceRoot: { newRoot: '$routeDetails' } }"
    })
    public List<Route> getTop3RoutesWithMaxAverageRating();


    @Aggregation(pipeline = {
        // Me quedo con las compras con rating 1 en su review
        "{ $match: { 'review.rating': 1 } }",
        // Me quedo solo con el Id de la ruta
        "{ $project: { routeId: '$route.$id' } }",
        // Junto los ID para evitar repetidos
        "{ $group: { _id: '$routeId' } }",
        // Cambio el nombre _id a RouteId
        "{ $project: { routeId: '$_id' } }", 
        // Busco las rutas con (logicamente según entiendo) con rating 1
        "{ $lookup: { from: 'route', localField: 'routeId', foreignField: '_id' , as: 'routes' } }",
        // Desarmo las rutas, quedandome una combinacion routeId-route
        "{ $unwind: '$routes' }",
        // Reemplazo los documentos de forma que me quede solo route
        "{ $replaceRoot: { newRoot: '$routes' } }"
    })
    public List<Route> getRoutesWithMinRating ();


    @Aggregation(pipeline = {
        // Projecto lo que necesito
        "{ $project: { routeId: '$route.$id', purchaseId: '$_id' } }",
        // Agrupo por la ruta y cuento sus compras
        "{ $group: { _id: '$routeId', cantPurchases: { $sum: 1 } } }",
        // Cambio el nombre _id a RouteId
        "{ $project: { routeId: '$_id', cantPurchases: 1 } }", 
        // Ordeno de mator a menor segun la cantidad de compras
        "{ $sort: { cantPurchases: -1 } }",
        // Me quedo con la ruta con mayor cantidad de compras
        "{ $limit: 1 }",
        // Me traigo la ruta
        "{ $lookup: { from: 'route', localField: '_id', foreignField: '_id', as: 'route' } }",
        // Desarmo la ruta
        "{ $unwind: '$route' }",
        // Reemplazar root por el documento original de la ruta
        "{ $replaceRoot: { newRoot: '$route' } }"
    })
    public Route getMostBestSellingRoute();
}
