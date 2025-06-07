package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Supplier;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, ObjectId> {
    
    public Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);

    @Aggregation(pipeline = {
        "{$lookup: {from: 'service', localField: '_id', foreignField: 'supplier', as: 'serviceData'}}",
        "{$unwind: '$serviceData'}",
        "{$addFields: {purchaseCount: { $size: '$serviceData.purchases' }}}",
        "{$group: {_id: '$_id', count: {$sum: '$purchaseCount'}}}",
        "{$sort: {count: 1}}",
        "{$limit: ?0}",
        "{$lookup: {from: 'supplier', localField: '_id', foreignField: '_id', as: 'sup'}}",
        "{$unwind: '$sup'}",
        "{$replaceRoot: {newRoot: '$sup'}}"
    })
    public List<Supplier> getTopNSuppliersInPurchases(int n);

    @Aggregation(pipeline = {
    "{$unwind: '$services'}",
    "{$group: {_id: '$_id', count: {$sum: 1}}}",
    "{$sort: {count: -1}}",
    "{$limit: 1}",
    "{$project: {_id: 0, count: 1}}"
    })
    public Long getMaxServicesOfSupplier();
}
