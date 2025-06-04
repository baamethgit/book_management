package ba.ameth.projects.bookManagement.dto;

import ba.ameth.projects.bookManagement.entities.ReadingSession;

import java.time.LocalDate;

public class ReadingSessionDto {
    private Long id;
    private LocalDate readingDate;
    private Integer pagesRead;
    private String bookTitle;

    public ReadingSessionDto() {}

    public ReadingSessionDto(ReadingSession session) {
        this.id = session.getId();
        this.readingDate = session.getReadingDate();
        this.pagesRead = session.getPagesRead();
        this.bookTitle = session.getBook().getTitle();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDate readingDate) { this.readingDate = readingDate; }

    public Integer getPagesRead() { return pagesRead; }
    public void setPagesRead(Integer pagesRead) { this.pagesRead = pagesRead; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
}
