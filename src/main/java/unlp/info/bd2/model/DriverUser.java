package unlp.info.bd2.model;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("Driver")
public class DriverUser extends User {

    @Column(name = "expedient", nullable = false) //Check nullability
    private String expedient;

    @ManyToMany(mappedBy = "driverList", cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private List<Route> routes;
}
