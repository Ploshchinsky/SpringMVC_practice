package ploton.SpringMVC_BookLibrary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ploton.SpringMVC_BookLibrary.exception.EntityValidationException;
import ploton.SpringMVC_BookLibrary.model.entity.Book;
import ploton.SpringMVC_BookLibrary.model.service.BookService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books/")
@Transactional
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello from BookController!";
    }

    @PostMapping("/")
    public ResponseEntity<Book> save(@RequestBody Book entity) {
        Optional<Book> book = Optional.of(bookService.save(entity));
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(entity));
    }

    @GetMapping("/")
    public Page<Book> getAllPageable(Pageable pageable) {
        return bookService.getAllPageable(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable("id") Integer id) {
        Optional<Book> optionalBook = Optional.of(bookService.getById(id));
        return optionalBook.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateById(@PathVariable("id") Integer id,
                                           Map<String, Object> updates) {
        Optional<Book> optionalBook = Optional.of(bookService.updateById(id, updates));
        return optionalBook.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(bookService.deleteById(id), HttpStatus.OK);
    }
}
