package unlp.info.bd2.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Supplier;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, ObjectId> {
    
    public Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);
}
