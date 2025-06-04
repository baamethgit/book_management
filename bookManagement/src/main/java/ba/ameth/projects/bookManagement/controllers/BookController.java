package ba.ameth.projects.bookManagement.controllers;


import ba.ameth.projects.bookManagement.dto.BookDto;
import ba.ameth.projects.bookManagement.dto.CategoryDto;
import ba.ameth.projects.bookManagement.entities.User;
import ba.ameth.projects.bookManagement.service.BookService;
import ba.ameth.projects.bookManagement.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getMyBooks(@RequestParam(required = false) Long categoryId, Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<BookDto> books;

        if (categoryId != null) {
            books = bookService.getUserBooksByCategory(user.getId(), categoryId);
        } else {
            books = bookService.getUserBooks(user.getId());
        }

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();
            BookDto book = bookService.getBookById(id, user.getId());
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Map<String, Object> request, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();
            String title = (String) request.get("title");
            String author = (String) request.get("author");
            Integer totalPages = (Integer) request.get("totalPages");
            Long categoryId = request.get("categoryId") != null ? ((Number) request.get("categoryId")).longValue() : null;
            String notes = (String) request.get("notes");

            // Validations
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le titre est obligatoire"));
            }
            if (author == null || author.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "L'auteur est obligatoire"));
            }
            if (totalPages == null || totalPages <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le nombre de pages doit être positif"));
            }

            BookDto book = bookService.createBook(title.trim(), author.trim(), totalPages, categoryId, notes, user);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Map<String, Object> request, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();

            String title = (String) request.get("title");
            String author = (String) request.get("author");
            Integer totalPages = request.get("totalPages") != null ? ((Number) request.get("totalPages")).intValue() : null;
            Long categoryId = request.get("categoryId") != null ? ((Number) request.get("categoryId")).longValue() : null;
            String notes = (String) request.get("notes");

            // Validations basiques (tu peux adapter)
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le titre est obligatoire"));
            }
            if (author == null || author.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "L'auteur est obligatoire"));
            }
            if (totalPages == null || totalPages <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le nombre de pages doit être positif"));
            }

            BookDto updatedBook = bookService.updateBook(id, title.trim(), author.trim(), totalPages, categoryId, notes, user.getId());
            return ResponseEntity.ok(updatedBook);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<?> updateBookProgress(@PathVariable Long id, @RequestBody Map<String, Object> request, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();


            Integer currentPage = request.get("currentPage") != null ? ((Number) request.get("currentPage")).intValue() : null;
            Boolean isFinished = request.get("isFinished") != null ? (Boolean) request.get("isFinished") : false;


            if (currentPage == null || currentPage < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le nombre de pages lues doit être positif ou égal à zéro"));
            }

            BookDto updatedBook = bookService.updateProgress(id, currentPage, isFinished, null, user.getId());
            return ResponseEntity.ok(updatedBook);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

        @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();
            bookService.deleteBook(id, user.getId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}