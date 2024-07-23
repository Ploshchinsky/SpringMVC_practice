package ploton.SpringMVC_BookLibrary.model.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface Serviceable<T> {
    T save(T entity);

    List<T> getAll();

    T getById(Integer id);

    T updateById(Integer id, Map<String, Object> updates);

    Integer deleteById(Integer id);

}
