package unlp.info.bd2.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Service;

@Repository
public interface ServiceRepository extends MongoRepository<Service, ObjectId> {
    public Optional<Service> findByNameAndSupplierId(String name, ObjectId Id);

}
