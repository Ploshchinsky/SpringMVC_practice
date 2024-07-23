package ploton.SpringMVC_BookLibrary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ploton.SpringMVC_BookLibrary.model.entity.Author;
import ploton.SpringMVC_BookLibrary.model.service.AuthorService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/authors")
@Transactional
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Author Controller";
    }

    @PostMapping("/")
    public ResponseEntity<Author> save(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.save(author), HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    public Page<Author> getAllPageable(Pageable pageable) {
        return authorService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(authorService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Author> updateById(@PathVariable("id") Integer id,
                                             @RequestBody Map<String, Object> updates) {
        Optional<Author> optionalAuthor = Optional.of(authorService.updateById(id, updates));
        return optionalAuthor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(authorService.deleteById(id), HttpStatus.OK);
    }
}
