package ploton.SpringMVC_BookLibrary.model.Repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ploton.SpringMVC_BookLibrary.model.entity.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT author FROM Author author WHERE author.id = :id")
    Optional<Author> findByWithBlock(@Param("id") Integer id);
}
