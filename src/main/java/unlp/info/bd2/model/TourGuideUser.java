package unlp.info.bd2.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class TourGuideUser extends User {

    @Column(name = "education", nullable = true) //Analyze later if it's actually nullable, or should be mandatory
    private String education;

    //Implement later
    private List<Route> routes;

}
