package unlp.info.bd2.model;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ITEM_SERVICE")
public class ItemService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_id", referencedColumnName = "id", nullable = false)
    private Purchase purchase;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", referencedColumnName = "id", nullable = false)
    private Service service;

    public float calculatePrice(){
        return this.quantity * this.service.getPrice();
    }

    public ItemService (Service service, int quantity, Purchase purchase) {
        this.service = service;
        this.quantity = quantity;
        this.purchase = purchase;
        purchase.addItem(this);
        service.addItemService(this);
    }

    public ItemService(){

    }
}
