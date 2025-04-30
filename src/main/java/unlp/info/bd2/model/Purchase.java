package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import unlp.info.bd2.utils.ToursException;

@Data
@Entity
@Table(name = "Purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "totalPrice", nullable = false)
    private float totalPrice;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route;

    @OneToOne(mappedBy = "purchase", cascade = {}, orphanRemoval = true)
    private Review review;

    @OneToMany(mappedBy = "purchase", cascade = {}, orphanRemoval = true)
    private List<ItemService> itemServiceList = new ArrayList<ItemService>();

    public Purchase (String code, Date date, Route route, User user) throws ToursException {
        this.code = code;
        this.totalPrice = route.getPrice();
        this.date = date;
        this.user = user;
        this.route = route;
        this.route.addPurchase(this);
        this.user.addPurchase(this);
    }

    public Purchase(){
        
    }

    public void addItem (ItemService item, float price) {
        this.itemServiceList.add(item);
        this.totalPrice += price;
    }

    public Review addReview (int rating, String comment) {
        Review review = new Review(rating, comment, this);
        this.review = review;
        return this.review;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, code, totalPrice, date); // Excluir review
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(id, purchase.id);
    }
}
