package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Stop")
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", nullable = false, length = 1023) //Recheck length and nullability
    private String description;

    @ManyToMany(mappedBy = "stops", cascade = {}, fetch=FetchType.LAZY)
    private List<Route> routes = new ArrayList<Route>();

    public Stop() {
        // Default constructor for JPA
    }

    public Stop(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
