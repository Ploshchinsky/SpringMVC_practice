package ploton.SpringMVC_ProductOrderCustomer.repository;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ploton.SpringMVC_ProductOrderCustomer.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
