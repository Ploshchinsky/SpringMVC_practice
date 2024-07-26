package ploton.SpringMVC_ProductOrderCustomer.service;

import org.springframework.stereotype.Service;
import ploton.SpringMVC_ProductOrderCustomer.entity.Order;
import ploton.SpringMVC_ProductOrderCustomer.entity.Product;
import ploton.SpringMVC_ProductOrderCustomer.exception.EntityValidationException;
import ploton.SpringMVC_ProductOrderCustomer.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProductService implements ProductServiceable {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String save(String entityJson) {
        Product product = EntityUtils.deSerialize(entityJson, Product.class);
        validate(product);
        productRepository.save(product);
        return entityJson;
    }

    @Override
    public List<String> findAll() {
        return productRepository.findAll().stream()
                .peek(this::validate)
                .map(EntityUtils::serialize)
                .toList();
    }

    @Override
    public String findById(Integer id) {
        return productRepository.findById(id)
                .map(EntityUtils::serialize)
                .orElseThrow(() -> new NoSuchElementException("Product ID - " + id));
    }

    @Override
    public String updateById(Integer id, Map<String, Object> updates) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product ID - " + id));
        update(product, updates);
        validate(product);
        productRepository.save(product);
        return EntityUtils.serialize(product);
    }

    @Override
    public Integer deleteById(Integer id) {
        productRepository.deleteById(id);
        return id;
    }

    @Override
    public void update(Product entity, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            switch (key) {
                case "productId":
                    if (value instanceof Integer)
                        entity.setProductId((Integer) value);
                    break;
                case "name":
                    if (value instanceof String)
                        entity.setName((String) value);
                    break;
                case "description":
                    if (value instanceof String)
                        entity.setDescription((String) value);
                    break;
                case "price":
                    if (value instanceof Double)
                        entity.setPrice((Double) value);
                    break;
                case "quantityInStock":
                    if (value instanceof Integer)
                        entity.setQuantityInStock((Integer) value);
                default:
                    throw new IllegalArgumentException("Product Wrong Filed - [" + key + " - " + value + "]");
            }
        });
    }

    @Override
    public void validate(Product entity) {
        List<String> errors = new ArrayList<>();
        if (!entity.getName().matches("^[a-zA-Z0-9 .-_]+$")) {
            errors.add("Validation Error - Product [Name] - " + entity.getName());
        }
        if (!entity.getDescription().matches("^[a-zA-Z0-9 .-_]+$")) {
            errors.add("Validation Error - Product [Description] - " + entity.getDescription());
        }
        if (!entity.getPrice().toString().matches("^\\d+.\\d+$")) {
            errors.add("Validation Error - Product [Coast] - " + entity.getPrice());
        }
        if (!entity.getQuantityInStock().toString().matches("^\\d+$")) {
            errors.add("Validation Error - Product [In Storage Amount] - " + entity.getQuantityInStock());
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors.toString());
        }
    }
}
