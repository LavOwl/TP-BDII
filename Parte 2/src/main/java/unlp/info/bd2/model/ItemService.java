package unlp.info.bd2.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
public class ItemService {

    @Id
    private ObjectId id;

    private int quantity;

    @Transient //OK, so, a bit of a fucked up field, don't really know what to do about it
                //We can set it as Transient, which is all well and good, up and until we actually
                //may need to access it... because the DB will not auto-fill the field
                //we could also just set it as @DBRef, but that would make the children reference
                //their parent, the one in which they are embedded, which is a tiny bit of an anti-pattern
                //or we could do without it altogether, pero ya estaba acá de antes, so nose.
    private Purchase purchase;

    @DBRef
    private Service service;

    public ItemService(int quantity, Service service, Purchase purchase){
        this.id = new ObjectId();
        this.quantity = quantity;
        this.service = service;
        this.purchase = purchase;
        this.purchase.addItemService(this);
        this.service.addItemService(this);
    }

    public float getCost(){
        return this.quantity * service.getPrice();
    }
}
