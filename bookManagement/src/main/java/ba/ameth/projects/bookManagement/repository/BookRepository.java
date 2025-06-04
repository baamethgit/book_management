package ba.ameth.projects.bookManagement.repository;

import ba.ameth.projects.bookManagement.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUserId(Long userId);
    List<Book> findByUserIdAndCategoryId(Long userId, Long categoryId);
    List<Book> findByUserIdAndIsFinished(Long userId, Boolean isFinished);
    List<Book> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Book> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.user.id = :userId AND b.isFinished = true")
    Long countFinishedBooksByUserId(@Param("userId") Long userId);
}