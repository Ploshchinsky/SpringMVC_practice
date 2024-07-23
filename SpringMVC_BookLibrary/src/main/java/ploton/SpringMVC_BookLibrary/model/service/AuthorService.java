package ploton.SpringMVC_BookLibrary.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ploton.SpringMVC_BookLibrary.exception.EntityValidationException;
import ploton.SpringMVC_BookLibrary.model.Repository.AuthorRepository;
import ploton.SpringMVC_BookLibrary.model.entity.Author;
import ploton.SpringMVC_BookLibrary.model.entity.Book;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthorService implements Serviceable<Author> {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author save(Author entity) {
        authorValidate(entity);
        return authorRepository.save(entity);
    }

    @Override
    public List<Author> getAll() {
        return (List<Author>) authorRepository.findAll();
    }

    public Page<Author> getAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Override
    public Author getById(Integer id) {
        Optional<Author> author = authorRepository.findByWithBlock(id);
        return author.orElseThrow(() -> new NoSuchElementException("Author ID - " + id));
    }

    @Override
    public Author updateById(Integer id, Map<String, Object> updates) {
        Optional<Author> optionalAuthor = authorRepository.findByWithBlock(id);
        optionalAuthor.ifPresent(author -> authorUpdate(author, updates));
        return optionalAuthor.orElseThrow(() -> new NoSuchElementException("Author ID - " + id));
    }

    @Override
    public Integer deleteById(Integer id) {
        if (authorRepository.existsById(id))
            authorRepository.deleteById(id);
        return id;
    }

    //Utility's

    private void authorValidate(Author entity) {
        if (!entity.getName().matches("^([a-zA-Z]+)( [a-zA-Z]+){1,3}$")) {
            throw new EntityValidationException("Wrong Author Field: [Name] - " + entity.getName());
        }
        if (!entity.getAge().toString().matches("^[0-9]{1,3}$")) {
            throw new EntityValidationException("Wrong Author Field: [Age] - " + entity.getAge());
        }
        if (!entity.getBirthCity().matches("([a-zA-Z]+)( [a-zA-Z]+)?$")) {
            throw new EntityValidationException("Wrong Author Field: [Birth City] - " + entity.getBirthCity());
        }
    }

    private Author authorUpdate(Author entity, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            switch (key) {
                case "id":
                    if (value instanceof Integer)
                        entity.setId((Integer) value);
                    break;
                case "name":
                    if (value instanceof String)
                        entity.setName((String) value);
                    break;
                case "age":
                    if (value instanceof Integer)
                        entity.setAge((Integer) value);
                    return;
                case "birthCity":
                    if (value instanceof String)
                        entity.setBirthCity((String) value);
                    break;
                case "books":
                    if (value instanceof Book)
                        entity.setBooks((List<Book>) value);
                    break;
            }
        });
        return entity;
    }
}
