package unlp.info.bd2.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Review {

    @Id
    private ObjectId id;

    private int rating;

    private String comment;

    @Transient
    private Purchase purchase;

    public Review(int rating, String comment, Purchase purchase){
        //this.id = new ObjectId(); //No quieren que tenga id :(
        this.rating = rating;
        this.comment = comment;
        this.purchase = purchase;
        this.purchase.setReview(this);
    }

}
