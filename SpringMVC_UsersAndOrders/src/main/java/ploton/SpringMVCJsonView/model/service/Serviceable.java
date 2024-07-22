package ploton.SpringMVCJsonView.model.service;

import java.util.List;
import java.util.Map;

public interface Serviceable<T> {
    public T save(T entity);

    public List<T> getAll();

    public T getById(Integer id);

    public boolean exist(T entity);

    public boolean deleteById(Integer id);

    public void delete(T entity);

    public T updateById(Integer id, Map<String, Object> updates);

}
