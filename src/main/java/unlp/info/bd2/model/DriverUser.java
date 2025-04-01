package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import lombok.Data;
import lombok.EqualsAndHashCode;
import unlp.info.bd2.utils.ToursException;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("Driver")
public class DriverUser extends User {

    @Column(name = "expedient", nullable = true)
    private String expedient;

    @ManyToMany(mappedBy = "driverList", cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private List<Route> routes = new ArrayList<Route>();

    public DriverUser(String username, String password, String name, String email, Date birthdate, String phoneNumber,
            String expedient) {
        super(username, password, name, email, birthdate, phoneNumber);
        this.expedient = expedient;
    }

    public DriverUser(){
        super();
    }

    public DriverUser(String username, String password, String name, String email, Date birthdate, String phoneNumber) {
        super(username, password, name, email, birthdate, phoneNumber);
    }

    public void addRoute(Route route) {
        if (this.routes == null) {
            this.routes = new ArrayList<Route>();
        }
        this.routes.add(route);
    }

    @Override
    @PreRemove
    protected void checkForFKReferences() throws ToursException {
        if((this.routes != null && !this.routes.isEmpty())){
            throw new ToursException("El usuario no puede ser desactivado");
        }
        super.checkForFKReferences();
    }
}
