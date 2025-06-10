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
        "{$addFields: {services_ids: '$services'}}",
        "{$lookup: {from: 'purchase', let: { providedServices: '$services' }, pipeline: [{ $match: {$expr: {$gt: [{ $size: {$filter: {input: '$itemServiceList.service',as: 'service',cond: { $in: ['$$service', '$$providedServices'] }}} },0]}} }]as: 'matchingPurchases'}}",
        "{$addFields: {count: {$size: '$matchingPurchases'}}}",
        "{$sort: {count: -1}}",
        "{$unset: ['services', 'matchingPurchases', 'count']}",
        "{$limit: ?0}"
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
