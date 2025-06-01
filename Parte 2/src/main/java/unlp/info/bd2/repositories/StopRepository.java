package unlp.info.bd2.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Stop;

@Repository
public interface StopRepository extends MongoRepository<Stop, ObjectId> {
    
    public List<Stop> findByNameStartingWith(String prefix);
}
