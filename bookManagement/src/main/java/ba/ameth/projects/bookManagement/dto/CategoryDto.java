package ba.ameth.projects.bookManagement.dto;

import ba.ameth.projects.bookManagement.entities.Category;

public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String coverImage;
    private Integer bookCount;

    public CategoryDto() {}

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.coverImage = category.getCoverImage();
        this.bookCount = category.getBooks().size();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public Integer getBookCount() { return bookCount; }
    public void setBookCount(Integer bookCount) { this.bookCount = bookCount; }
}
