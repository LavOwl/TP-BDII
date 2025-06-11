package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import unlp.info.bd2.utils.ToursException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "purchase")
@NoArgsConstructor
public class Purchase {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String code;

    private float totalPrice;

    private Date date;

    @DBRef(lazy = true)
    private User user;

    @DBRef(lazy = true)
    private Route route;

    //Embedded by default
    private Review review;

    //Embedded by default
    private List<ItemService> itemServiceList = new ArrayList<ItemService>();

    public Purchase(String code, Date date, Route route, User user) throws ToursException{
        route.addPurchase(this);
        this.route = route;

        this.code = code;
        this.date = date;
        this.totalPrice = route.getPrice();

        this.user = user;
        user.addPurchase(this);
    }

    public Purchase addItemService(ItemService itemService){
        itemService.setPurchase(this);
        this.itemServiceList.add(itemService);
        this.totalPrice += itemService.getCost();
        return this;
    }

    public void setItemServiceList(List<ItemService> itemServiceList){
        itemServiceList.stream().forEach(i -> i.setPurchase(this));
        this.itemServiceList = itemServiceList;
    }

    public void setReview(Review review){
        this.review = review;
        this.review.setPurchase(this);
    }

}
