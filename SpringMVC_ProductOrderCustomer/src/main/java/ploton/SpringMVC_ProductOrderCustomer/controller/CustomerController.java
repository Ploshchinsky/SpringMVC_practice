package ploton.SpringMVC_ProductOrderCustomer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ploton.SpringMVC_ProductOrderCustomer.service.CustomerServiceable;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerServiceable customerService;

    public CustomerController(CustomerServiceable customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/")
    public ResponseEntity<String> save(@RequestBody String jsonEntity) {
        return new ResponseEntity<>(customerService.save(jsonEntity), HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    public ResponseEntity<List<String>> getAll() {
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable("id") Integer id,
                                             @RequestBody Map<String, Object> updates) {
        return new ResponseEntity<>(customerService.updateById(id, updates), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(customerService.deleteById(id), HttpStatus.OK);
    }
}
