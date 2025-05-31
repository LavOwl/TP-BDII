package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Document
public class Purchase {

    @Id
    private ObjectId id;

    private String code;

    private float totalPrice;

    private Date date;

    @DBRef
    private User user;

    @DBRef
    private Route route;

    //Embedded by default
    private Review review;

    //Embedded by default
    private List<ItemService> itemServiceList;

}
