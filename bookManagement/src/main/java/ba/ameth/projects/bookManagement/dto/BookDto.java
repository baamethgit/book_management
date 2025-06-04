package ba.ameth.projects.bookManagement.dto;

import ba.ameth.projects.bookManagement.entities.Book;

import java.time.LocalDateTime;

public class BookDto {
    private Long id;
    private String title;
    private String author;
    private Integer totalPages;
    private String coverImage;
    private Integer currentPage;
    private Boolean isFinished;
    private String notes;
    private Double progressPercentage;
    private Integer remainingPages;
    private LocalDateTime createdAt;
    private CategoryDto category;

    public BookDto() {}

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.totalPages = book.getTotalPages();
        this.coverImage = book.getCoverImage();
        this.currentPage = book.getCurrentPage();
        this.isFinished = book.getIsFinished();
        this.notes = book.getNotes();
        this.progressPercentage = book.getProgressPercentage();
        this.remainingPages = book.getRemainingPages();
        this.createdAt = book.getCreatedAt();
        if (book.getCategory() != null) {
            this.category = new CategoryDto(book.getCategory());
        }
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

    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; }

    public Integer getRemainingPages() { return remainingPages; }
    public void setRemainingPages(Integer remainingPages) { this.remainingPages = remainingPages; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public CategoryDto getCategory() { return category; }
    public void setCategory(CategoryDto category) { this.category = category; }
}