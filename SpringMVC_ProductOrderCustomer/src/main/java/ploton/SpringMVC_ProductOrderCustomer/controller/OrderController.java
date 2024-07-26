package ploton.SpringMVC_ProductOrderCustomer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ploton.SpringMVC_ProductOrderCustomer.service.OrderServiceable;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderServiceable orderService;

    public OrderController(OrderServiceable orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public ResponseEntity<String> save(@RequestBody String jsonEntity) {
        return new ResponseEntity<>(orderService.save(jsonEntity), HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    public ResponseEntity<List<String>> getAll() {
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchById(@PathVariable("id") Integer id,
                                            @RequestBody Map<String, Object> updates) {
        return new ResponseEntity<>(orderService.updateById(id, updates), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Integer> deleteById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(orderService.deleteById(id), HttpStatus.OK);
    }
}
