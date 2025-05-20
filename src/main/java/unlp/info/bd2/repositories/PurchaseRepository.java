package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Service;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
     public Optional<Purchase> findByCode(String code);

    public List<Purchase> findTop10ByItemServiceListIsNotEmptyOrderByTotalPriceDesc();

    public long countAllByDateIsBetween(Date start, Date end);
    
    public List<Purchase> findByItemServiceListService(Service service);

    public List<Purchase> findByUserUsername(String username);
}
