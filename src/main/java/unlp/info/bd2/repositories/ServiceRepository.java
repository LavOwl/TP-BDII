package unlp.info.bd2.repositories;



import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import unlp.info.bd2.model.Service;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    public Optional<Service> findByNameAndSupplierId(String name, Long supplierId);
    public List<Service> findByItemServiceListIsEmpty();

    @Query("SELECT DISTINCT item.service " +
            "FROM ItemService item " +
            "GROUP BY item.service " +
            "ORDER BY SUM(item.quantity) DESC") //Can't handle aggregations with QueryMethods(Order By Sum)
    public List<Service> getMostDemandedServices (Pageable pageable);

}
