package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import unlp.info.bd2.utils.ToursException;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("user")
@Table(name = "APP_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 255, unique = true, updatable = false)
    private String username;

    @Column(name = "password", nullable = false, length = 511)
    private String password;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "date", nullable = false)
    private Date birthdate;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Setter(AccessLevel.PRIVATE)
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchaseList = new ArrayList<Purchase>();

    public User(String username, String password, String name, String email, Date birthdate, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
    }

    public User(){
    }

    @PreRemove
    protected void checkForFKReferences() throws ToursException {
        if(!this.isActive()){
            throw new IllegalStateException("El usuario se encuentra desactivado");
        }
        if (purchaseList != null && !purchaseList.isEmpty()) {
            this.active = false;
            throw new IllegalStateException();
        }
    }

    public void addPurchase(Purchase purchase) {
        purchaseList.add(purchase);
    }

    public void desactivar() {
        this.active = false;
    }
}
