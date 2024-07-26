package ploton.SpringMVC_ProductOrderCustomer.service;

import ploton.SpringMVC_ProductOrderCustomer.entity.Order;
import ploton.SpringMVC_ProductOrderCustomer.entity.Product;

import java.util.List;
import java.util.Map;

public interface OrderServiceable
        extends EntityValidatable<Order>, EntityUpdatable<Order> {
    String save(String entityJson);

    List<String> findAll();

    String findById(Integer id);

    String updateById(Integer id, Map<String, Object> updates);

    Integer deleteById(Integer id);
}
