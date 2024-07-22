package ploton.SpringMVCJsonView.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ploton.SpringMVCJsonView.model.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
