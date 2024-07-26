package ploton.SpringMVC_ProductOrderCustomer.service;

import org.springframework.stereotype.Service;
import ploton.SpringMVC_ProductOrderCustomer.entity.Customer;
import ploton.SpringMVC_ProductOrderCustomer.entity.Order;
import ploton.SpringMVC_ProductOrderCustomer.exception.EntityValidationException;
import ploton.SpringMVC_ProductOrderCustomer.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class CustomerService implements CustomerServiceable {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public String save(String entityJson) {
        Customer customer = EntityUtils.deSerialize(entityJson, Customer.class);
        validate(customer);
        customerRepository.save(customer);
        return entityJson;
    }

    @Override
    public List<String> findAll() {
        return customerRepository.findAll().stream()
                .peek(this::validate)
                .map(EntityUtils::serialize)
                .toList();
    }

    @Override
    public String findById(Integer id) {
        return customerRepository.findById(id)
                .map(EntityUtils::serialize)
                .orElseThrow(() -> new NoSuchElementException("Customer ID - " + id));
    }

    @Override
    public String updateById(Integer id, Map<String, Object> updates) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer ID - " + id));
        update(customer, updates);
        validate(customer);
        customerRepository.save(customer);
        return EntityUtils.serialize(customer);
    }

    @Override
    public Integer deleteById(Integer id) {
        customerRepository.deleteById(id);
        return id;
    }

    @Override
    public void update(Customer entity, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            switch (key) {
                case "customerId":
                    if (value instanceof Integer)
                        entity.setCustomerId((Integer) value);
                    break;
                case "name":
                    if (value instanceof String)
                        entity.setName((String) value);
                    break;
                case "email":
                    if (value instanceof String)
                        entity.setEmail((String) value);
                    break;
                case "phone":
                    if (value instanceof String)
                        entity.setPhone((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Customer Wrong Filed - [" + key + " - " + value + "]");
            }
        });
    }

    @Override
    public void validate(Customer entity) {
        List<String> errors = new ArrayList<>();
        if (!entity.getName().matches("^[\\w ]+$")) {
            errors.add("Validation Error - Customer [Name] - " + entity.getName());
        }
        if (!entity.getPhone().matches("^(\\+7)?8?\\d{10}$")) {
            errors.add("Validation Error - Customer [Phone] - " + entity.getPhone());
        }
        if (!entity.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.add("Validation Error - Customer [Email] - " + entity.getEmail());
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors.toString());
        }
    }
}
