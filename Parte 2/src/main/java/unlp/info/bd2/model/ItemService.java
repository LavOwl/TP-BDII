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

    @Transient
    private Purchase purchase;

    @DBRef(lazy = true)
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
