package ploton.SpringMVCJsonView.model.service;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploton.SpringMVCJsonView.exception.UserNotFoundException;
import ploton.SpringMVCJsonView.model.entity.Order;
import ploton.SpringMVCJsonView.model.entity.User;
import ploton.SpringMVCJsonView.model.repository.OrderRepository;
import ploton.SpringMVCJsonView.model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Transactional
public class OrderService implements Serviceable<Order> {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Order save(Order entity) {
        userIdCheck(entity);
        return orderRepository.save(entity);
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public boolean exist(Order entity) {
        return orderRepository.exists(Example.of(entity));
    }

    @Override
    public boolean deleteById(Integer id) {
        orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Order ID [" + id + "]"));
        orderRepository.deleteById(id);
        return true;
    }

    @Override
    public void delete(Order entity) {
        orderRepository.delete(entity);
    }

    @Override
    public Order updateById(Integer id, Map<String, Object> updates) {
        return null;
    }

    private User getUserIfPresent(Order entity) {
        return userRepository.findByIdWithLock(entity.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User ID - " + entity.getUser().getId()));
    }

    private static void userIdCheck(Order entity) {
        if (entity.getUser() == null || entity.getUser().getId() == null) {
            throw new IllegalArgumentException("Order must have a valid user");
        }
    }
}
