package unlp.info.bd2.model;

import java.util.Date;
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
@DiscriminatorValue("TourGuide")
public class TourGuideUser extends User {

    @Column(name = "education", nullable = true) //Analyze later if it's actually nullable, or should be mandatory
    private String education;

    @ManyToMany(mappedBy = "tourGuideList", cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private List<Route> routes;

    public TourGuideUser(String username, String password, String name, String email, Date birthdate,
            String phoneNumber, String education) {
        super(username, password, name, email, birthdate, phoneNumber);
        this.education = education;
    }
}
