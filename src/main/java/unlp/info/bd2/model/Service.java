package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "description", nullable = false) //Recheck nullability
    private String description;

    @OneToMany(mappedBy = "service", cascade = {}, orphanRemoval = true, fetch=FetchType.LAZY)
    private List<ItemService> itemServiceList = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch=FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier supplier;

    public Service() {
        // Default constructor
    }

    public Service(String name, float price, String description, Supplier supplier) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.supplier = supplier;
        this.supplier.addService(this);
    }
    public void addItemService(ItemService itemService) {
        this.itemServiceList.add(itemService);
    }
}
