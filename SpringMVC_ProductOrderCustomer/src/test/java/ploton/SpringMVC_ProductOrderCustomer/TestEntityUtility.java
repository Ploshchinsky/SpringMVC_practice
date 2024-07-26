package ploton.SpringMVC_ProductOrderCustomer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ploton.SpringMVC_ProductOrderCustomer.entity.Customer;
import ploton.SpringMVC_ProductOrderCustomer.entity.Order;
import ploton.SpringMVC_ProductOrderCustomer.entity.Product;
import ploton.SpringMVC_ProductOrderCustomer.service.EntityUtils;

import java.time.LocalDate;
import java.util.List;

public class TestEntityUtility {
    private List<Product> products;
    private List<Order> orders;

    @BeforeEach
    public void setUp() {
        products = List.of(
                new Product(1, "iPhone 15", "Mobile phone by Apple",
                        900, 89000.00),
                new Product(2, "MacBook Pro 2024", "Notebook by Apple",
                        830, 245000.00)
        );

        orders = List.of(
                new Order(1, "Address Test 1", 89000.00,
                        Order.OrderStatus.NEW, LocalDate.now(), List.of(products.get(0)), null),
                new Order(2, "Address Test 2", 245000.00,
                        Order.OrderStatus.CANCELED, LocalDate.now(), List.of(products.get(1)), null)
        );
    }

    @Test
    public void testSerializationProduct() throws Exception {
        Product expected = new Product(
                1, "iPhone 15", "Mobile phone by Apple",
                900, 89000.00);

        String json = EntityUtils.serialize(expected);
        Product actual = EntityUtils.deSerialize(json, Product.class);

        System.out.println("json:\n" + json);
        System.out.println(actual);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationOrder() throws Exception {
        Order expected = new Order();
        expected.setOrderId(1);
        expected.setProducts(products);
        expected.setTotalPrice(products.stream().mapToDouble(Product::getPrice).sum());
        expected.setOrderDate(LocalDate.now());

        String json = EntityUtils.serialize(expected);

        Order actual = EntityUtils.deSerialize(json, Order.class);

        System.out.println(json);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getTotalPrice(), actual.getTotalPrice());
    }

    @Test
    public void testSerializationCustomer() throws Exception {
        Customer expected = new Customer();
        expected.setCustomerId(1);
        expected.setName("Name Test");
        expected.setPhone("+79991230987");
        expected.setEmail("test@gmail.com");

        String json = EntityUtils.serialize(expected);

        Customer actual = EntityUtils.deSerialize(json, Customer.class);

        System.out.println(json);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }
}
