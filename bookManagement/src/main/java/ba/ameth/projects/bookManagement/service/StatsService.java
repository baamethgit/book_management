package ba.ameth.projects.bookManagement.service;

import ba.ameth.projects.bookManagement.dto.ReadingSessionDto;
import ba.ameth.projects.bookManagement.dto.StatsDto;
import ba.ameth.projects.bookManagement.repository.BookRepository;
import ba.ameth.projects.bookManagement.repository.ReadingSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private BookRepository bookRepository;
    private ReadingSessionRepository readingSessionRepository;

    public StatsService(BookRepository bookRepository, ReadingSessionRepository readingSessionRepository) {
        this.bookRepository = bookRepository;
        this.readingSessionRepository = readingSessionRepository;
    }

    public StatsDto getUserStats(Long userId) {
        StatsDto stats = new StatsDto();

        // Stats de base
        stats.setTotalBooks(bookRepository.countByUserId(userId));
        stats.setFinishedBooks(bookRepository.countFinishedBooksByUserId(userId));

        // Pages lues
        Long totalPages = readingSessionRepository.getTotalPagesReadByUserId(userId);
        stats.setTotalPagesRead(totalPages != null ? totalPages : 0L);

        // Pages cette semaine
        LocalDate weekStart = LocalDate.now().minusDays(7);
        Long pagesThisWeek = readingSessionRepository.getPagesReadBetweenDates(userId, weekStart, LocalDate.now());
        stats.setPagesThisWeek(pagesThisWeek != null ? pagesThisWeek : 0L);

        // Pages ce mois
        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
        Long pagesThisMonth = readingSessionRepository.getPagesReadBetweenDates(userId, monthStart, LocalDate.now());
        stats.setPagesThisMonth(pagesThisMonth != null ? pagesThisMonth : 0L);

        // Moyenne par jour (sur les 30 derniers jours)
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        Long pagesLast30Days = readingSessionRepository.getPagesReadBetweenDates(userId, thirtyDaysAgo, LocalDate.now());
        if (pagesLast30Days != null && pagesLast30Days > 0) {
            stats.setAveragePagesPerDay(Math.round(pagesLast30Days / 30.0 * 10.0) / 10.0);
        } else {
            stats.setAveragePagesPerDay(0.0);
        }

        return stats;
    }

    public List<ReadingSessionDto> getRecentSessions(Long userId, int limit) {
        return readingSessionRepository.findByBookUserIdOrderByReadingDateDesc(userId)
                .stream()
                .limit(limit)
                .map(ReadingSessionDto::new)
                .collect(Collectors.toList());
    }
}