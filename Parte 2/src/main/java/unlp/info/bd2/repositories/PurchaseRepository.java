package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.User;
import java.util.Date;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {
    public Optional<Purchase> findByCode(String code);

    @Aggregation(pipeline = {
        "{$project: {totalPrice: 1, _id:0, user:'$user.$id'}}",
        "{$match: {totalPrice: {$gte: ?0}}}",
        "{$group: {_id: '$user'}}",
        "{$lookup: {from: 'users', localField: '_id', foreignField: '_id', as: 'user'}}",
        "{$unwind: '$user'}",
        "{$replaceRoot: {newRoot: '$user'}}"
    })
    public List<User> getUsersOfPurchasesOver(float amount);

    @Aggregation(pipeline = {
        "{$unwind: '$itemServiceList'}",
        "{$project: {itemServiceList: '$itemServiceList.service'}}",
        "{$lookup: {from: 'supplier', localField: 'itemServiceList', foreignField: 'services', as: 'supplier'}}",
        "{$group: {_id: {_id:'$_id' ,supplier: '$supplier'}}}",
        "{$replaceRoot: {newRoot: '$_id'}}",
        "{$unwind: '$supplier'}",
        "{$group: {_id: '$supplier', count: {$sum: 1}}}",
        "{$sort: {count: -1}}",
        "{$limit: ?0}",
        "{$replaceRoot: {newRoot: '$_id'}}"
    })
    public List<Supplier> getTopNMostPresentSuppliers(int n);


    public Long countByDateBetween(Date start, Date end);
}
