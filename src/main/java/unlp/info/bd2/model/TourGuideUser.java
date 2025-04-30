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
@DiscriminatorValue("tour_guide")
public class TourGuideUser extends User {

    @Column(name = "education", nullable = true) //Analyze later if it's actually nullable, or should be mandatory
    private String education;

    @ManyToMany(mappedBy = "tourGuideList", cascade = {})
    private List<Route> routes = new ArrayList<Route>();

    public TourGuideUser(String username, String password, String name, String email, Date birthdate,
            String phoneNumber, String education) {
        super(username, password, name, email, birthdate, phoneNumber);
        this.education = education;
    }

    public TourGuideUser(){
        super();
    }

    public void addRoute(Route route) {
        if (this.routes == null) {
            this.routes = new ArrayList<Route>();
        }
        this.routes.add(route);
        route.addTourGuide(this);
    }

    @Override
    @PreRemove
    protected void checkForFKReferences() throws ToursException {
        if((this.routes != null && !this.routes.isEmpty())){
            throw new IllegalStateException("El usuario no puede ser desactivado");
        }
        super.checkForFKReferences();
    }
}
