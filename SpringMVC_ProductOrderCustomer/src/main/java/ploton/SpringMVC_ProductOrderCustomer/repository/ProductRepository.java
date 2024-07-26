package ploton.SpringMVC_ProductOrderCustomer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ploton.SpringMVC_ProductOrderCustomer.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
