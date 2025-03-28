package unlp.info.bd2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    //Implement
    private Purchase purchase;
}
