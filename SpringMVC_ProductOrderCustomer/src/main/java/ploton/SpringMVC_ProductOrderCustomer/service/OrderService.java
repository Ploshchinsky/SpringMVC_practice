package ploton.SpringMVC_ProductOrderCustomer.service;

import org.springframework.stereotype.Service;
import ploton.SpringMVC_ProductOrderCustomer.entity.Customer;
import ploton.SpringMVC_ProductOrderCustomer.entity.Order;
import ploton.SpringMVC_ProductOrderCustomer.entity.Product;
import ploton.SpringMVC_ProductOrderCustomer.exception.EntityValidationException;
import ploton.SpringMVC_ProductOrderCustomer.repository.CustomerRepository;
import ploton.SpringMVC_ProductOrderCustomer.repository.OrderRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class OrderService implements OrderServiceable {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public String save(String entityJson) {
        Order order = EntityUtils.deSerialize(entityJson, Order.class);
        validate(order);
        orderRepository.save(order);
        return entityJson;
    }

    @Override
    public List<String> findAll() {
        return orderRepository.findAll().stream()
                .peek(this::validate)
                .map(EntityUtils::serialize)
                .toList();
    }

    @Override
    public String findById(Integer id) {
        return orderRepository.findById(id)
                .map(EntityUtils::serialize)
                .orElseThrow(() -> new NoSuchElementException("Order ID - " + id));
    }

    @Override
    public String updateById(Integer id, Map<String, Object> updates) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order ID - " + id));
        update(order, updates);
        validate(order);
        orderRepository.save(order);
        return EntityUtils.serialize(order);
    }

    @Override
    public Integer deleteById(Integer id) {
        orderRepository.deleteById(id);
        return id;
    }


    @Override
    public void update(Order entity, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            switch (key) {
                case "orderId":
                    if (value instanceof Integer)
                        entity.setOrderId((Integer) value);
                    break;
                case "shippingAddress":
                    if (value instanceof String)
                        entity.setShippingAddress((String) value);
                    break;
                case "totalPrice":
                    if (value instanceof Double)
                        entity.setTotalPrice((Double) value);
                    break;
                case "products":
                    if (value instanceof List)
                        entity.setProducts((List<Product>) value);
                case "customer":
                    if (value instanceof Map) {
                        Map<String, Object> customerMap = (Map<String, Object>) value;
                        Integer customerId = (Integer) customerMap.get("id");
                        Customer customer = customerRepository.findById(customerId)
                                .orElseThrow(() -> new NoSuchElementException("Customer ID - " + customerId));
                        entity.setCustomer(customer);
                    }
                    break;
                case "orderDate":
                    if (value instanceof LocalDate)
                        entity.setOrderDate((LocalDate) value);
                    break;
                case "orderStatus":
                    if (value instanceof String)
                        entity.setOrderStatus(Order.OrderStatus.valueOf((String) value));
                    break;
                default:
                    throw new IllegalArgumentException("Order Wrong Filed - [" + key + " - " + value + "]");
            }
        });
    }

    @Override
    public void validate(Order entity) {
        List<String> errors = new ArrayList<>();
        if (!entity.getShippingAddress().matches("^[\\w ]+$")) {
            errors.add("Validation Error - Order [Address] - " + entity.getShippingAddress());
        }
        if (!entity.getTotalPrice().toString().matches("^\\d+.\\d+$")) {
            errors.add("Validation Error - Product [Coast] - " + entity.getTotalPrice());
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors.toString());
        }
    }
}
