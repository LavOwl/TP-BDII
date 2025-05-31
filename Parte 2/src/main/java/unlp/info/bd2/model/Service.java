package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.List;

@Data
@Document
public class Service {

    @Id
    private ObjectId id;

    private String name;

    private float price;

    private String description;

    @DBRef
    private List<ItemService> itemServiceList;

    @DBRef //Might be better to have the Services embedded, if so, then maybe this should be transient.
    private Supplier supplier;

}
