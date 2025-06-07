package unlp.info.bd2.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.Route;

@Repository
public interface RouteRepository extends MongoRepository<Route, ObjectId> {
    public List<Route> findAllByPriceIsLessThan(float price);

    @Aggregation(pipeline = {
        // Este no funciona el primer lookup porque varia en tipos, en Route es ObjectID, y en Purchase es DBRef, y no puedo acceder a el ObjectID en el lookup
        // Junto las rutas y sus compras
        "{ $lookup: { from: 'purchase', localField: 'id', foreignField: 'route', as: 'purchases' } }",
        // Desarmo las compras, quedandome una combinacion ruta-compra 
        "{ $unwind: '$purchases' }",
        // Me quito la ruta y me quedo con las compras
        "{ $replaceRoot: { newRoot: '$purchases._id' } }",
        // Me quedo con las compras con rating 1 en su review
        "{ $match: { 'review.rating': 1 } }",
        // Me quedo solo con el Id de la ruta
        "{ $project: { routeId: '$route.$id' } }",
        // Junto los ID para evitar repetidos
        "{ $group: { _id: '$routeId' } }",
        // Cambio el nombre _id a RouteId
        "{ $project: { routeId: '$_id' } }", 
        // Busco las rutas con (logicamente según entiendo) con rating 1
        "{ $lookup: { from: 'route', localField: 'routeId', foreignField: 'id' , as: 'routes' } }",
        // Desarmo las rutas, quedandome una combinacion routeId-route
        "{ $unwind: '$routes' }",
        // Reemplazo los documentos de forma que me quede solo route
        "{ $replaceRoot: { newRoot: '$routes' } }"

        // No hay compras en la ruta, por eso no funciona este
        /*// Desarmo las compras
        "{ $unwind: '$purchases' }",
        // Me quedo solo con las compras
        "{ $replaceRoot: { newRoot: '$purchases' } }",
        // Me quedo con las compras con rating 1 en su review
        "{ $match: { 'review.rating': 1 } }",
        // Me quedo solo con el Id de la ruta
        "{ $project: { routeId: '$route' } }",
        // Junto los ID para evitar repetidos
        "{ $group: { _id: '$routeId' } }",
        // Cambio el nombre _id a RouteId
        "{ $project: { routeId: '$_id' } }", 
        // Busco las rutas con (logicamente según entiendo) con rating 1
        "{ $lookup: { from: 'route', localField: 'routeId', foreignField: '_id' , as: 'routes' } }",
        // Desarmo las rutas, quedandome una combinacion routeId-route
        "{ $unwind: '$routes' }",
        // Reemplazo los documentos de forma que me quede solo route
        "{ $replaceRoot: { newRoot: '$routes' } }"*/
    })
    public List<Route> getRoutesWithMinRating ();


    @Aggregation(pipeline = {
        // Este no funciona el primer lookup porque varia en tipos, en Route es ObjectID, y en Purchase es DBRef, y no puedo acceder a el ObjectID en el lookup
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
        // Cambio el nombre _id a RouteId
        "{ $project: { routeId: '$_id', avgRating: 1 } }", 
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

        /*// Desarmo las compras
        "{ $unwind: '$purchases' }",
        // Me quedo solo con las compras
        "{ $replaceRoot: { newRoot: '$purchases' } }",
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
        "{ $lookup: { from: 'route', localField: 'routeId', foreignField: '_id' , as: 'routes' } }",
        // Desarmo las rutas, quedandome una combinacion routeId-route
        "{ $unwind: '$routes' }",
        // Reemplazo los documentos de forma que me quede solo route
        "{ $replaceRoot: { newRoot: '$routes' } }"*/

        /*// Desarmo las compras
        "{ $unwind: '$purchases' }",
        // Me quedo solo con las compras
        "{ $replaceRoot: { newRoot: '$purchases' } }",
        // Filtramos compras con rating válido
        "{ $match: { 'review': { $exists: true } } }",
        // Me quedo con la ruta y el rating
        "{ $project: { routeId: '$route.$id', rating: '$review.rating' } }",
        // Agrupamos por ruta, calculamos promedio y cantidad
        "{ $group: { _id: '$routeId', averageRating: { $avg: '$rating' } } }",
        // Ordenamos por promedio descendente
        "{ $sort: { averageRating: -1 } }",
        // Top 3
        "{ $limit: 3 }",
        // Buscamos info de la ruta
        "{ $lookup: { from: 'route', localField: '_id', foreignField: '_id', as: 'routeDetails' } }",
        // Desempaquetamos el resultado
        "{ $unwind: '$routeDetails' }",
        // Proyectamos los campos deseados
        "{ $replaceRoot: { newRoot: '$routeDetails' } }"*/
    })
    public List<Route> getTop3RoutesWithMaxAverageRating ();


   @Aggregation(pipeline = {
        // Este no funciona el primer lookup porque varia en tipos, en Route es ObjectID, y en Purchase es DBRef, y no puedo acceder a el ObjectID en el lookup
        // Junto las rutas y sus compras
        "{ $lookup: { from: 'purchase', localField: '_id', foreignField: 'route', as: 'purchases' } }",
        // Desarmo las compras de la ruta
        "{ $unwind: '$purchases' }",
        // Proyecto la ruta y la compra
        "{ $project: { routeId: '$_id', purchaseId: '$purchases._id' } }",
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
    //No funciona y nose porque

    @Aggregation(pipeline = {
        // Desarmo los DriverUser
        "{ $unwind: '$driverList' }",
        // Me quedo con los DriverUser solamente
        "{ $replaceRoot: { newRoot: '$driverList' } }",
        // Los agrupo para evitar repetidos
        "{ $group: { _id: '_id' } }",
        // Añado un campo para contar la cantidad de rutas que tiene cada uno
        "{ $addFields: { count: { $size:'$routes' } } }",
        // Los ordeno de mayor a menor cantidad de rutas
        "{ $sort: { count: -1 } }",
        // Me quedo con el de mayor cantidad de rutas
        "{ $limit: 1 }"
        //"{ $match: { username: 'userD2' } }", // muy gracioso 
    })
    public DriverUser getDriverUserWithMoreRoutes ();
    // funciona peor que el otro
}
