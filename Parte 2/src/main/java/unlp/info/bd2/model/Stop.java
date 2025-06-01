package unlp.info.bd2.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
public class Stop {

    @Id
    private ObjectId id;

    private String name;

    private String description;

    public Stop(String name, String description){
        this.name = name;
        this.description = description;
    }
}
