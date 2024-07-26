package ploton.SpringMVC_ProductOrderCustomer.service;

public interface EntityValidatable<T> {
    void validate(T entity);
}
