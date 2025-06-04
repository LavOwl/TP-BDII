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
}
