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
import unlp.info.bd2.model.Route;

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


    @Aggregation(pipeline = {
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
}
