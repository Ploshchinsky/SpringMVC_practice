package ploton.SpringMVCJsonView.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ploton.SpringMVCJsonView.exception.UserNotFoundException;
import ploton.SpringMVCJsonView.model.entity.Order;
import ploton.SpringMVCJsonView.model.entity.User;
import ploton.SpringMVCJsonView.model.entity.Views;
import ploton.SpringMVCJsonView.model.repository.UserRepository;
import ploton.SpringMVCJsonView.model.service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String helloWorld() {
        return "Hello World!";
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity<Order> save(@RequestBody Order order) {
        User user = userRepository.findByIdWithLock(order.getUser().getId()).orElse(null);
        order.setUser(user);
        return new ResponseEntity<>(orderService.save(order), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") Integer id) {
        Optional<Order> order = Optional.ofNullable(orderService.getById(id));
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAll() {
        Optional<List<Order>> orders = Optional.ofNullable(orderService.getAll());
        return orders.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
