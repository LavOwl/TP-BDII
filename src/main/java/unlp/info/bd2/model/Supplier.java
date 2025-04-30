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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "Supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name", nullable = false, length = 255)
    private String businessName;

    @Column(name = "authorization_number", nullable = false, length = 255, unique = true)
    private String authorizationNumber;

    @OneToMany(mappedBy = "supplier", cascade = {}, orphanRemoval = true, fetch=FetchType.LAZY)
    private List<Service> services = new ArrayList<>();

    public Supplier() {
        // Default constructor
    }

    public Supplier(String businessName, String authorizationNumber) {
        this.businessName = businessName;
        this.authorizationNumber = authorizationNumber;
    }

    public void addService(Service service) {
        services.add(service);
    }
}
