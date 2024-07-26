package ploton.SpringMVC_ProductOrderCustomer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;

    private String shippingAddress;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private LocalDate orderDate;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public static enum OrderStatus {
        NEW,
        IN_PROGRESS,
        DONE,
        CANCELED;
    }
}
