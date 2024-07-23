package ploton.SpringMVC_BookLibrary.model.Repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ploton.SpringMVC_BookLibrary.model.entity.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT book FROM Book book WHERE book.id = :id")
    Optional<Book> findByWithBlock(@Param("id") Integer id);
}
