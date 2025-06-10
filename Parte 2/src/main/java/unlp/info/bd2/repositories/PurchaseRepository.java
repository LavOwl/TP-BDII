package unlp.info.bd2.repositories;

import java.util.Optional;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Purchase;
import java.util.Date;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {
    public Optional<Purchase> findByCode(String code);
    public Long countByDateBetween(Date start, Date end);
}
