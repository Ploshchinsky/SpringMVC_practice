package ploton.SpringMVC_ProductOrderCustomer.service;

import java.util.Map;

public interface EntityUpdatable<T> {
    void update(T entity, Map<String, Object> updates);
}
