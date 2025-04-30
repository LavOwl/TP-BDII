package unlp.info.bd2.model;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating", nullable = false) //Check if adding restrictions to the value would be advisable (1 <= rating <= 5)
    private int rating;

    @Column(name = "comment", nullable = true, length = 1023) //Recheck nullability and length
    private String comment;

    @OneToOne(cascade = {}) //Cascading other operations can lead to infinite loops.
    @JoinColumn(name = "purchase_id", referencedColumnName = "id", nullable = false)
    private Purchase purchase;


    public Review (int rating, String comment, Purchase purchase) {
        this.rating = rating;
        this.comment = comment;
        this.purchase = purchase;
    }

    public Review(){
        
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, rating, comment); // Excluir purchase
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }
}
