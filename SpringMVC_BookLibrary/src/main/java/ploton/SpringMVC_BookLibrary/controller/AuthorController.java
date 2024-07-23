package ploton.SpringMVC_BookLibrary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ploton.SpringMVC_BookLibrary.model.entity.Author;
import ploton.SpringMVC_BookLibrary.model.service.AuthorService;

import java.util.List;
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
    @ResponseBody
    public String hello() {
        return "Hello from Author Controller";
    }

    @PostMapping("/")
    public ResponseEntity<Author> save(@RequestBody Author author) {
        Optional<Author> optionalAuthor = Optional.of(authorService.save(author));
        return optionalAuthor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(author));
    }

    @GetMapping("/")
    public Page<Author> getAllPageable(Pageable pageable) {
        Optional<Page<Author>> page = Optional.of(authorService.getAll(pageable));
        return page.get();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable("id") Integer id) {
        Optional<Author> optionalAuthor = Optional.of(authorService.getById(id));
        return optionalAuthor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
