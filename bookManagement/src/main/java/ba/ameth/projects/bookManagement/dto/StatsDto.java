package ba.ameth.projects.bookManagement.dto;

public class StatsDto {
    private Long totalBooks;
    private Long finishedBooks;
    private Long totalPagesRead;
    private Long pagesThisWeek;
    private Long pagesThisMonth;
    private Double averagePagesPerDay;

    public StatsDto() {}

    // Getters & Setters
    public Long getTotalBooks() { return totalBooks; }
    public void setTotalBooks(Long totalBooks) { this.totalBooks = totalBooks; }

    public Long getFinishedBooks() { return finishedBooks; }
    public void setFinishedBooks(Long finishedBooks) { this.finishedBooks = finishedBooks; }

    public Long getTotalPagesRead() { return totalPagesRead; }
    public void setTotalPagesRead(Long totalPagesRead) { this.totalPagesRead = totalPagesRead; }

    public Long getPagesThisWeek() { return pagesThisWeek; }
    public void setPagesThisWeek(Long pagesThisWeek) { this.pagesThisWeek = pagesThisWeek; }

    public Long getPagesThisMonth() { return pagesThisMonth; }
    public void setPagesThisMonth(Long pagesThisMonth) { this.pagesThisMonth = pagesThisMonth; }

    public Double getAveragePagesPerDay() { return averagePagesPerDay; }
    public void setAveragePagesPerDay(Double averagePagesPerDay) { this.averagePagesPerDay = averagePagesPerDay; }
}
