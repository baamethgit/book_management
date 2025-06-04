package ba.ameth.projects.bookManagement.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reading_sessions")
public class ReadingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate readingDate;

    @Column(nullable = false)
    private Integer pagesRead;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Constructeurs
    public ReadingSession() {}

    public ReadingSession(LocalDate readingDate, Integer pagesRead, Book book) {
        this.readingDate = readingDate;
        this.pagesRead = pagesRead;
        this.book = book;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDate readingDate) { this.readingDate = readingDate; }

    public Integer getPagesRead() { return pagesRead; }
    public void setPagesRead(Integer pagesRead) { this.pagesRead = pagesRead; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}