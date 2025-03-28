package unlp.info.bd2.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "totalPrice", nullable = false)
    private float totalPrice;

    @Column(name = "date", nullable = false)
    private Date date;

    //Implement
    private User user;

    //Implement
    private Route route;

    //Implement
    private Review review;

    //Implement
    private List<ItemService> itemServiceList;
}
