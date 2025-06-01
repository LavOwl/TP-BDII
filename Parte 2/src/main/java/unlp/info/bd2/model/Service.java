package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.ArrayList;
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
    private List<ItemService> itemServiceList = new ArrayList<ItemService>();

    @DBRef //Might be better to have the Services embedded, if so, then maybe this should be transient.
    private Supplier supplier;

    public Service(String name, float price, String description, Supplier supplier){
        this.name = name;
        this.price = price;
        this.description = description;
        this.supplier = supplier;
        this.supplier.addService(this);
    }

    public Service updatePrice(float price){
        this.price = price;
        return this;
    }

    public void addItemService(ItemService itemService){
        this.itemServiceList.add(itemService);
    }

}
