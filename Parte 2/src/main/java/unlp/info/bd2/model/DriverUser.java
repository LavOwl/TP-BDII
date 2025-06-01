package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "users")
@EqualsAndHashCode(callSuper = true)
public class DriverUser extends User {

    private String expedient;

    @DBRef
    private List<Route> routes = new ArrayList<Route>();

    public DriverUser(String username, String password, String name, String email, Date birthdate, String phoneNumber, String expedient) {
        super(username, password, name, email, birthdate, phoneNumber);
        this.expedient = expedient;
    }

}
