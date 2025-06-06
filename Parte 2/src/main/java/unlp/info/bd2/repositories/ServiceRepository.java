package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Supplier;

@Repository
public interface ServiceRepository extends MongoRepository<Service, ObjectId> {
    public Optional<Service> findByNameAndSupplierId(String name, ObjectId Id);

    @Aggregation(pipeline = {
        "{$unwind: '$itemServiceList'}",
        "{$project: {_id: 0, itemServiceList:1, supplier:'$supplier.$id'}}",
        "{$group: {_id: '$supplier', count: {$sum: 1}}}",

        "{$sort: {count: -1}}",
        "{$limit: ?0}",
        "{$lookup: {from: 'supplier', localField: '_id', foreignField: '_id', as: 'sup'}}",
        "{$unwind: '$sup'}",
        "{$replaceRoot: {newRoot: '$sup'}}"
    })
    public List<Supplier> getTopNSuppliersInItemsSold(int n); //Debería estar en SupplierRepository, pero te la recontra re regalo.


    @Aggregation(pipeline = {
        // Desarmo los itemServideList que estan en Service
        "{ $unwind: '$itemServiceList' }",
        // Projecto el id del Service y la cantidad que vendio en cada item
        "{ $project: { serviceId: '$_id', cant: '$itemServiceList.quantity' } }",
        // Agrupo por el id del Service sumando todas las cantidades vendidas
        "{ $group: { _id: '$serviceId', count: { $sum: '$cant' } } }",
        // Cambio el nombre _id a serviceId para mayor comprensión
        "{ $project: { serviceId: '$_id', count: 1 } }",
        // Ordeno de mayor a menor vendido
        "{ $sort: { count: -1 } }",
        // Me quedo con el más vendido
        "{ $limit: 1 }",
        // Me traigo el servicio, quedandome serviceId-service
        "{ $lookup: { from: 'service', localField: 'serviceId', foreignField: '_id', as: 'service' } }",
        // Desarmo el servicio
        "{ $unwind: '$service' }",
        // Me quedo solo con el servicio
        "{ $replaceRoot: { newRoot: '$service' } }"
    })  
    public Service getMostDemandedService ();
    // Este me sorprendio, a veces pasa y a veces no, no siempre encuentra el más demandado, podría probar haciendolo
    // con un lookup a Purchases a ver si hay un problema de consistencia con el itemServiceList actual
}
