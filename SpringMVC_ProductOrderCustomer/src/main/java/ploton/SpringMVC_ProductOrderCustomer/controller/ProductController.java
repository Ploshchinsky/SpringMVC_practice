package ploton.SpringMVC_ProductOrderCustomer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ploton.SpringMVC_ProductOrderCustomer.service.ProductServiceable;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductServiceable productService;

    public ProductController(ProductServiceable productServiceable) {
        this.productService = productServiceable;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from ProductController";
    }

    @PostMapping("/")
    public ResponseEntity<String> save(@RequestBody String jsonEntity) {
        return new ResponseEntity<>(productService.save(jsonEntity), HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    public ResponseEntity<List<String>> getAll() {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable("id") Integer id,
                                             @RequestBody Map<String, Object> updates) {
        return new ResponseEntity<>(productService.updateById(id, updates), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(productService.deleteById(id), HttpStatus.OK);
    }
}
