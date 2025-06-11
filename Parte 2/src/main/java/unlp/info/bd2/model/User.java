package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unlp.info.bd2.utils.ToursException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "users")
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String username;

    private String password;

    private String name;

    private String email;

    private Date birthdate;

    private String phoneNumber;

    @Setter(AccessLevel.PRIVATE)
    private boolean active;

    @DBRef(lazy = true)
    private List<Purchase> purchaseList = new ArrayList<Purchase>();

    public User(String username, String password, String name, String email, Date birthdate, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.active = true;
    }

    public void addPurchase(Purchase purchase){
        this.purchaseList.add(purchase);
    }

    public void logicalRemoval() throws ToursException{
        if(!active){
            throw new ToursException("Usuario ya desactivado");
        }
        if(purchaseList.size() != 0){
            this.active = false;
            throw new RuntimeException("Constraints Error");
        }
    }
}
