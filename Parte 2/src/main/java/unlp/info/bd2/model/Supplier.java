package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@NoArgsConstructor
public class Supplier {

    @Id
    private ObjectId id;

    private String businessName;

    @Indexed(unique = true)
    private String authorizationNumber;

    @DBRef
    private List<Service> services = new ArrayList<Service>();

    public Supplier(String businessName, String authorizationNumber){
        this.businessName = businessName;
        this.authorizationNumber = authorizationNumber;
    }

    public void addService(Service service){
        this.services.add(service);
    }

}
