package ploton.SpringMVC_ProductOrderCustomer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ploton.SpringMVC_ProductOrderCustomer.controller.OrderController;
import ploton.SpringMVC_ProductOrderCustomer.entity.Customer;
import ploton.SpringMVC_ProductOrderCustomer.entity.Order;
import ploton.SpringMVC_ProductOrderCustomer.entity.Product;
import ploton.SpringMVC_ProductOrderCustomer.service.EntityUtils;
import ploton.SpringMVC_ProductOrderCustomer.service.OrderServiceable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(OrderController.class)
public class TestOrderController {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    OrderServiceable orderService;
    List<Product> products;
    List<Customer> customers;
    Order order;
    String orderJson;
    Map<String, Object> updates;

    @BeforeEach
    void setUp() {
        products = List.of(
                new Product(1, "iPhone 15", "Mobile Phone by Apple", 900, 110000.0),
                new Product(2, "Xiaomi 13", "Mobile Phone by Xiaomi", 830, 85000.0),
                new Product(3, "Fender Telecaster 1989", "Electric Guitar by Fender", 22, 210000.0)
        );
        customers = List.of(
                new Customer(1, "Petrov Petr", "petrov@yandex.ru", "89000009988"),
                new Customer(2, "Ivanova Maria", "ivanova@gmail.com", "+79221112233")
        );
        order = new Order();
        order.setOrderId(1);
        order.setShippingAddress("Mayakovskaya Street 15 app 23");
        order.setOrderStatus(Order.OrderStatus.NEW);
        order.setOrderDate(LocalDate.now());
        order.setProducts(products);
        order.setCustomer(customers.get(1));
        order.setTotalPrice(products.stream().mapToDouble(Product::getPrice).sum());
        orderJson = EntityUtils.serialize(order);
        updates = Map.of("shippingAddress", "Updated Address", "orderStatus", "CANCELED");
    }

    @Test
    void testSave() throws Exception {
        when(orderService.save(anyString())).thenReturn(orderJson);

        mockMvc.perform(post("/api/v1/orders/").content(orderJson))
                .andExpect(status().isAccepted())
                .andExpect(content().string(orderJson));
    }

    @Test
    void testGetAll() throws Exception {
        when(orderService.findAll()).thenReturn(List.of(orderJson));

        mockMvc.perform(get("/api/v1/orders/"))
                .andExpect(status().isOk())
                .andExpect(content().string(EntityUtils.serialize(List.of(orderJson))));
    }

    @Test
    void testGetById() throws Exception {
        when(orderService.findById(1)).thenReturn(orderJson);

        mockMvc.perform(get("/api/v1/orders/{1}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(EntityUtils.serialize(order)));
    }

    @Test
    void testGetById_WrongId() throws Exception {
        when(orderService.findById(anyInt())).thenThrow(new NoSuchElementException("Order ID - " + anyInt()));

        mockMvc.perform(get("/api/v1/orders/{1}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateById() throws Exception {
        Order updatedOrder = order;
        updatedOrder.setOrderStatus(Order.OrderStatus.valueOf((String) updates.get("orderStatus")));
        updatedOrder.setShippingAddress((String) updates.get("shippingAddress"));

        when(orderService.updateById(anyInt(), anyMap())).thenReturn(EntityUtils.serialize(updatedOrder));

        mockMvc.perform(patch("/api/v1/orders/{1}", 1)
                        .contentType(MediaType.APPLICATION_JSON
                        )
                        .content(EntityUtils.serialize(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(content().string(EntityUtils.serialize(updatedOrder)));
    }

    @Test
    void testDeleteById() throws Exception {
        when(orderService.deleteById(anyInt())).thenReturn(1);

        mockMvc.perform(delete("/api/v1/orders/{1}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void testDeleteById_WrongId() throws Exception {
        when(orderService.deleteById(666)).thenThrow(new NoSuchElementException("Order ID - " + 666));

        mockMvc.perform(delete("/api/v1/orders/{666}", 666))
                .andExpect(status().isNotFound());
    }
}
