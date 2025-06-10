package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import unlp.info.bd2.utils.ToursException;

@Getter
@Setter
@Document(collection = "users")
public class TourGuideUser extends User {

    private String education;

    @DBRef
    private List<Route> routes = new ArrayList<Route>();

    public TourGuideUser(String username, String password, String name, String email, Date birthdate, String phoneNumber, String education) {
        super(username, password, name, email, birthdate, phoneNumber);
        this.education = education;
    }

    public void addRoute(Route route){
        if(!this.routes.contains(route)){
            this.routes.add(route);
            route.addTourGuide(this);
        }
    }

    @Override
    public void logicalRemoval() throws ToursException{
        if(routes.size() != 0){
            throw new ToursException("Constraints Error");
        }
        super.logicalRemoval();
    }

}
