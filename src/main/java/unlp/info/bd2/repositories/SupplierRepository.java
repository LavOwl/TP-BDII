package unlp.info.bd2.repositories;

import unlp.info.bd2.model.Supplier;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SupplierRepository extends CrudRepository<Supplier, Long> {
    public Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);

    @Query("SELECT s FROM ItemService is JOIN is.purchase p JOIN is.service serv JOIN serv.supplier s GROUP BY s.id ORDER BY COUNT(p) DESC") //Can't handle aggregations with QueryMethods(Order By Count)
    List<Supplier> getTopNSuppliersInPurchases(Pageable pageable);

    @Query("SELECT s FROM ItemService is JOIN is.service serv JOIN serv.supplier s GROUP BY s.id ORDER BY COUNT(is) DESC") //Can't handle aggregations with QueryMethods(Order By Count)
    List<Supplier> getTopNSuppliersItemsSold(Pageable pageable);

    @Query("SELECT MAX(size(s.services)) FROM Supplier s")  //Can't handle aggregations with QueryMethods(Max(size(list)))
    public Long getMaxServicesOfSupplier();
}
