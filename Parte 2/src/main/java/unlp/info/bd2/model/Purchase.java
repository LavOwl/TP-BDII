package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Document
@NoArgsConstructor
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

    public Purchase(String code, Route route, User user){
        this.code = code;
        this.route = route;
        this.user = user;
        this.totalPrice = 0;
    }

    public Purchase(String code, Date date, Route route, User user){
        this.code = code;
        this.date = date;
        this.route = route;
        this.user = user;
        this.totalPrice = 0;
    }

    public Purchase addReview(Review review){
        this.review = review;
        return this;
        //Debería autosettearse en la review?? depende de como la persistamos supongo.
    }

    public Purchase addItemService(ItemService itemService){
        this.itemServiceList.add(itemService);
        this.totalPrice += itemService.getCost();
        return this;
    }

}
