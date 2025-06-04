package ba.ameth.projects.bookManagement.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Integer totalPages;

    private String coverImage;

    @Column(nullable = false)
    private Integer currentPage = 0;

    @Column(nullable = false)
    private Boolean isFinished = false;

    @Column(columnDefinition = "TEXT")
    private String notes; // Notes personnelles du user

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadingSession> readingSessions = new ArrayList<>();

    // Constructeurs
    public Book() {}

    public Book(String title, String author, Integer totalPages, User user) {
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.user = user;
    }

    // Méthodes utilitaires
    public Double getProgressPercentage() {
        if (totalPages == 0) return 0.0;
        return Math.round((currentPage * 100.0) / totalPages * 10.0) / 10.0; // Arrondi à 1 décimale
    }

    public Integer getRemainingPages() {
        return Math.max(0, totalPages - currentPage);
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public Integer getCurrentPage() { return currentPage; }
    public void setCurrentPage(Integer currentPage) { this.currentPage = currentPage; }

    public Boolean getIsFinished() { return isFinished; }
    public void setIsFinished(Boolean isFinished) { this.isFinished = isFinished; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<ReadingSession> getReadingSessions() { return readingSessions; }
    public void setReadingSessions(List<ReadingSession> readingSessions) { this.readingSessions = readingSessions; }
}