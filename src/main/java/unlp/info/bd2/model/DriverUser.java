package unlp.info.bd2.model;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class DriverUser extends User {

    @Column(name = "expedient", nullable = false) //Check nullability
    private String expedient;

    //Implement
    private List<Route> routes;
}
