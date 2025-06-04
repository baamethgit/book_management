package ba.ameth.projects.bookManagement.repository;


import ba.ameth.projects.bookManagement.entities.ReadingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingSessionRepository extends JpaRepository<ReadingSession, Long> {
    List<ReadingSession> findByBookIdOrderByReadingDateDesc(Long bookId);
    List<ReadingSession> findByBookUserIdOrderByReadingDateDesc(Long userId);

    @Query("SELECT SUM(rs.pagesRead) FROM ReadingSession rs WHERE rs.book.user.id = :userId")
    Long getTotalPagesReadByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(rs.pagesRead) FROM ReadingSession rs WHERE rs.book.user.id = :userId AND rs.readingDate BETWEEN :startDate AND :endDate")
    Long getPagesReadBetweenDates(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
