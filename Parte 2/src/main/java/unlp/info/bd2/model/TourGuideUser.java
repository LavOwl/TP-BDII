package unlp.info.bd2.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Document
public class TourGuideUser extends User {

    private String education;

    @DBRef
    private List<Route> routes;

}
