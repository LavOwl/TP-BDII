package unlp.info.bd2.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH}) //Cascading other operations can lead to infinite loops.
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH}) //Cascading other operations can lead to infinite loops.
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route; //Added Route reference to purchases to avoid null route_id through cascaded deletion.

    @OneToOne(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemService> itemServiceList;

    public Purchase (String code, Route route, User user) {
        this.code = code;
        this.totalPrice = route.getPrice();
        this.date = new Date();
        this.user = user;
        this.route = route;
    }

    public Purchase (String code, Date date, Route route, User user) {
        this.code = code;
        this.totalPrice = route.getPrice();
        this.date = date;
        this.user = user;
        this.route = route;
    }
}
