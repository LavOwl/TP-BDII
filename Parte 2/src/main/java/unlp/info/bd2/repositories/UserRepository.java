package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    public Optional<User> findByUsername(String username);
    public Optional<DriverUser> findDriverUserByUsername(String username);
    public Optional<TourGuideUser> findTourGuideUserByUsername(String username);

    @Aggregation({
        "{$match: {$expr: {$gte: [{$size: '$purchaseList'}, ?0]}}}"
    })
    public List<User> getUsersWithNPurchases(int n);

    @Aggregation(pipeline = {
        "{$addFields:{ count: {$size:'$purchaseList'}}}",
        "{$sort: {count: -1}}",
        "{$limit: ?0}"
    })
    public List<User> getTopNUsersMorePurchases(int n);


    @Aggregation(pipeline = {
        /*Este es el mio, tenia errores sintacticos y suspuestamente otros segun ChatGPT, dejo el que me tiro "supuestamente"
        arreglado*/

        //ETAPA DE OBTENER LAS RUTAS
        // Me quedo con los usuarios que tiene el atributo educacion, el cual son los TourGuideUser
        "{ $match: { education: { $exists: true } } }",
        // Como son los TourGuideUser y tienen rutas, las desarmo
        "{ $unwind: '$routes' }",
        // Me quedo solo con las rutas
        "{ $replaceRoot: { newRoot: '$routes._id' } }",
        // Agrupo las rutas de forma que elimino repetido (con el primer atributo) y me quedo con el documento original (con el segundo atributo)
        "{ $group: { '_id': '$_id', routeDoc: { '$first': '$$ROOT' } } }",
        // Reemplazo finalmente todas las rutas
        "{ $replaceRoot: { 'newRoot': '$routeDoc' } }",
        // Cambio el nombre de routeDoc a route
        //"{ $project: { route: '$routeDoc' } }",

        //ETAPA DE OBTENER RUTAS CON RATING 1
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
        // Cambio el nombre _id a RouteId
        "{ $project: { routeId: '$_id' } }", 
        // Busco las rutas con (logicamente según entiendo) con rating 1
        "{ $lookup: { from: 'route', localField: 'routeId', foreignField: 'id' , as: 'routes' } }",
        // Desarmo las rutas, quedandome una combinacion routeId-route
        "{ $unwind: '$routes._id' }",
        // Reemplazo los documentos de forma que me quede solo route
        "{ $replaceRoot: { newRoot: '$routes._id' } }",

        //ETAPA DE RECUPERAR A LOS TourGuideUser
        // Desarmo a los TourGuideList
        "{ $unwind: '$tourGuideList' }",
        // Me quedo solo con los TourGuideUser
        "{ $replaceRoot: { newRoot: '$tourGuideList._id' } }",
        // Agrupo los TourGuideUser de forma que elimino repetido (con el primer atributo) y me quedo con el documento original (con el segundo atributo)
        "{ $group: { '_id': '$_id', tourGuideUserDoc: { '$first': '$$ROOT' } } }",
        // Reemplazo finalmente todos los TourGuideUser
        "{ $replaceRoot: { 'newRoot': '$tourGuideUserDoc' } }"

        //ChatGPT
        /*// OBTENER RUTAS
        "{ $match: { education: { $exists: true } } }",
        "{ $unwind: '$routes' }",
        "{ $replaceRoot: { newRoot: '$routes._id' } }",
        "{ $group: { '_id': '$_id', routeDoc: { '$first': '$$ROOT' } } }",
        "{ $replaceRoot: { 'newRoot': '$routeDoc' } }",

        // RUTAS CON RATING 1
        "{ $lookup: { from: 'purchase', localField: 'id', foreignField: 'route', as: 'purchases' } }",
        "{ $unwind: '$purchases' }",
        "{ $replaceRoot: { newRoot: '$purchases' } }",
        "{ $match: { 'review.rating': 1 } }",
        "{ $project: { routeId: '$route' } }",
        "{ $group: { _id: '$routeId' } }",
        "{ $project: { routeId: '$_id' } }",
        "{ $lookup: { from: 'route', localField: 'routeId', foreignField: 'id' , as: 'routes' } }",
        "{ $unwind: '$routes' }",
        "{ $replaceRoot: { newRoot: '$routes' } }",

        // TOURGUIDEUSERS
        "{ $unwind: '$tourGuideList' }",
        "{ $replaceRoot: { newRoot: '$tourGuideList._id' } }",
        "{ $group: { '_id': '$_id', tourGuideUserDoc: { '$first': '$$ROOT' } } }",
        "{ $replaceRoot: { 'newRoot': '$tourGuideUserDoc' } }"*/
    })
    public List<TourGuideUser> getTourGuidesWithRating1 ();
    // De habitual, no funciona


    @Aggregation(pipeline = {
        // Me quedo con los usuarios que tiene el atributo expediente, el cual son los TourGuideUser
        "{ $match: { expedient: { $exists: true } } }",
        // Añado un campo para contar la cantidad de rutas que tiene cada uno
        "{ $addFields: { count: { $size:'$routes' } } }",
        // Los ordeno de mayor a menor cantidad de rutas
        "{ $sort: { count: -1 } }",
        // Me quedo con el de mayor cantidad de rutas
        "{ $limit: 1 }"
    })
    public DriverUser getDriverUserWithMoreRoutes ();
    // No se trae el DriverUser correcto
}
