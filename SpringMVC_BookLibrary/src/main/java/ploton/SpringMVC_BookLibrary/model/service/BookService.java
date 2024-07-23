package ploton.SpringMVC_BookLibrary.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploton.SpringMVC_BookLibrary.exception.EntityValidationException;
import ploton.SpringMVC_BookLibrary.model.Repository.AuthorRepository;
import ploton.SpringMVC_BookLibrary.model.Repository.BookRepository;
import ploton.SpringMVC_BookLibrary.model.entity.Author;
import ploton.SpringMVC_BookLibrary.model.entity.Book;

import java.util.*;

@Service
@Transactional
public class BookService implements Serviceable<Book> {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Book save(Book entity) {
        bookValidate(entity);
        if (authorRepository.existsById(entity.getAuthor().getId())) {
            entity.setAuthor(authorRepository.findByWithBlock(entity.getAuthor().getId()).get());
        }
        return bookRepository.save(entity);
    }

    @Override
    public List<Book> getAll() {
        return (List<Book>) bookRepository.findAll();
    }

    public Page<Book> getAllPageable(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book getById(Integer id) {
        Optional<Book> book = bookRepository.findByWithBlock(id);
        return book.orElseThrow(() -> new NoSuchElementException("Book ID - " + id));
    }

    @Override
    public Book updateById(Integer id, Map<String, Object> updates) {
        Optional<Book> optionalBook = bookRepository.findByWithBlock(id);
        optionalBook.ifPresent(book -> bookUpdate(book, updates));
        return optionalBook.orElseThrow(() -> new NoSuchElementException("Book ID - " + id));
    }

    @Override
    public Integer deleteById(Integer id) {
        bookRepository.deleteById(id);
        return id;
    }

    //Utility's
    private void bookValidate(Book entity) throws EntityValidationException {
        if (!entity.getName().matches("^([a-zA-Z]+)( [a-zA-Z]+){2,3}$")) {
            throw new EntityValidationException("Wrong Book Field: [Name] - " + entity.getName());
        }
        if (!entity.getYearOfCreation().toString().matches("^[0-9]{1,4}$")) {
            throw new EntityValidationException("Wrong Book Field: [Year Of Creation] - " + entity.getYearOfCreation());
        }
    }

    private Book bookUpdate(Book entity, Map<String, Object> updates) {
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            switch (entry.getKey()) {
                case "id":
                    if (entry.getValue() instanceof Integer)
                        entity.setId((Integer) entry.getValue());
                    break;
                case "name":
                    if (entry.getValue() instanceof String)
                        entity.setName((String) entry.getValue());
                    break;
                case "author":
                    if (entry.getValue() instanceof Author)
                        entity.setAuthor((Author) entry.getValue());
                    break;
                case "yearOfCreation":
                    if (entry.getValue() instanceof Integer)
                        entity.setYearOfCreation((Integer) entry.getValue());
                    break;
            }
        }
        bookValidate(entity);
        return entity;
    }
}
