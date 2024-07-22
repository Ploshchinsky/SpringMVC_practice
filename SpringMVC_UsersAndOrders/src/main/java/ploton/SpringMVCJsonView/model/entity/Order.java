package ploton.SpringMVCJsonView.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Details.class)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonView(Views.Details.class)
    @JsonBackReference
    private User user;

    @JsonView(Views.Details.class)
    private String description;

    @JsonView(Views.Details.class)
    private BigDecimal coast;

    @Enumerated(EnumType.STRING)
    @JsonView(Views.Details.class)
    private StatusType status;

    public enum StatusType {
        NEW,
        PACKAGING,
        DELIVERY,
        DONE,
        CANCELLED;
    }
}
