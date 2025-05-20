package unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import unlp.info.bd2.model.Stop;

public interface StopRepository extends CrudRepository<Stop, Long> {
    public List<Stop> findByNameStartingWith(String prefix);
}
