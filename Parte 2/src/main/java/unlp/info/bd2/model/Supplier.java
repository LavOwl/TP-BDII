package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.List;

@Data
@Document
public class Supplier {

    @Id
    private ObjectId id;

    private String businessName;

    private String authorizationNumber;

    @DBRef //Might be best to embed?
    private List<Service> services;

}
