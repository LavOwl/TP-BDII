package unlp.info.bd2.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Route;

@Repository
public interface RouteRepository extends MongoRepository<Route, ObjectId> {
    public List<Route> findAllByPriceIsLessThan(float price);

    @Aggregation(pipeline = {
        // Junto las rutas y sus compras
        "{ $lookup: { from: 'purchase', localField: 'id', foreignField: 'route', as: 'purchases' } }",
        // Desarmo las compras, quedandome una combinacion ruta-compra 
        "{ $unwind: '$purchases' }",
        // Me quito la ruta y me quedo con las compras
        "{ $replaceRoot: { newRoot: '$purchases._id' } }",
        // Me quedo con las compras con rating 1 en su review
        "{ $match: { 'review.rating': 1 } }",
        // Me quedo solo con el Id de la ruta
        "{ $project: { routeId: '$route' } }",
        // Junto los ID para evitar repetidos
        "{ $group: { _id: '$routeId' } }",
        // Busco las rutas con (logicamente según entiendo) con rating 1
        "{ $lookup: { from: 'route', localField: 'routeId', foreignField: 'id' , as: 'routes' } }",
        // Desarmo las rutas, quedandome una combinacion routeId-route
        "{ $unwind: '$routes._id' }",
        // Reemplazo los documentos de forma que me quede solo route
        "{ $replaceRoot: { newRoot: '$routes' } }"
    })
    public List<Route> getRoutesWithMinRating ();
    //No funciona y no se porque


    @Aggregation(pipeline = {
         // Junto las rutas y sus compras
        "{ $lookup: { from: 'purchase', localField: '_id', foreignField: 'route', as: 'purchases' } }",
        // Desarmo las compras, quedandome una combinacion ruta-compra 
        "{ $unwind: '$purchases' }",
        // Me quito la ruta y me quedo con las compras
        "{ $replaceRoot: { newRoot: '$purchases._id' } }",
        // Projecto la ruta y el rating
        "{ $project: { routeId: '$route', rating: '$review.rating' } }",
        // Agrupo las rutas calculando su rating promedio
        "{ $group: { _id: '$routeId', avgRating: { $avg: '$rating' } } }",
        // Ordeno de Mayor a Menor el Rating
        "{ $sort: { avgRating: -1 } }",
        // Me quedo con los tres mayores
        "{ $limit: 3 }",
        // Me quedo solo con el Id de la ruta
        "{ $project: { routeId: '$_id' } }",
        // Busco las 3 rutas con (logicamente según entiendo) con el mayor promedio de rating
        "{ $lookup: { from: 'route', localField: 'routeId', foreignField: '_id' , as: 'routes' } }",
        // Desarmo las rutas, quedandome una combinacion routeId-route
        "{ $unwind: '$routes' }",
        // Reemplazo los documentos de forma que me quede solo route
        "{ $replaceRoot: { newRoot: '$routes._id' } }"
    })
    public List<Route> getTop3RoutesWithMaxAverageRating ();
    //No funciona y no se porque


    @Aggregation(pipeline = {
        // Desarmo las compras
        "{ $unwind: '$purchases' }",
        // Projecto solo la ruta y las compras
        "{ $project: { routeId: '$_id', purchase: '$purchases._id' } }",
        // Agrupo las rutas y cuento sus compras
        "{ $group: { _id: '$routeId', cantPurchases: { $count: '$purchases._id' } } }",
        // Ordeno de Mayor a Menor las rutas por cantidad de compras
        "{ $sort: { cantPurchases: -1 } }",
        // Me quedo con la de mayor cantidad de compras
        "{ $limit: 1 }",
        // Recupero la ruta
        "{ $lookup: { from: 'route', localField: '_id', foreignField: '_id', as: 'route' }",
        // Reemplazo el documento por la ruta
        "{ $replaceRoot: { newRoot: 'route._id' } }"
    })
    public Route getMostBestSellingRoute ();
    //No funciona y no se porque
}
