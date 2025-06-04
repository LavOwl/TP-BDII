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
}
