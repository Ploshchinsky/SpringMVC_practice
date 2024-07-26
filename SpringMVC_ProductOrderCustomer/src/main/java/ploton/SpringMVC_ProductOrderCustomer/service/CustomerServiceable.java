package ploton.SpringMVC_ProductOrderCustomer.service;

import ploton.SpringMVC_ProductOrderCustomer.entity.Customer;
import ploton.SpringMVC_ProductOrderCustomer.entity.Order;

import java.util.List;
import java.util.Map;

public interface CustomerServiceable
        extends EntityValidatable<Customer>, EntityUpdatable<Customer> {
    String save(String entityJson);

    List<String> findAll();

    String findById(Integer id);

    String updateById(Integer id, Map<String, Object> updates);

    Integer deleteById(Integer id);
}
