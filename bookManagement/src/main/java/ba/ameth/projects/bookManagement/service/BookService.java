package ba.ameth.projects.bookManagement.service;

import ba.ameth.projects.bookManagement.dto.BookDto;
import ba.ameth.projects.bookManagement.entities.Book;
import ba.ameth.projects.bookManagement.entities.Category;
import ba.ameth.projects.bookManagement.entities.ReadingSession;
import ba.ameth.projects.bookManagement.entities.User;
import ba.ameth.projects.bookManagement.repository.BookRepository;
import ba.ameth.projects.bookManagement.repository.CategoryRepository;
import ba.ameth.projects.bookManagement.repository.ReadingSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;
    private ReadingSessionRepository readingSessionRepository;

    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository,ReadingSessionRepository readingSessionRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.readingSessionRepository = readingSessionRepository;
    }

    public List<BookDto> getUserBooks(Long userId) {
        return bookRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    public List<BookDto> getUserBooksByCategory(Long userId, Long categoryId) {
        return bookRepository.findByUserIdAndCategoryId(userId, categoryId)
                .stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    public BookDto getBookById(Long bookId, Long userId) {
        Book book = bookRepository.findByIdAndUserId(bookId, userId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));
        return new BookDto(book);
    }

    public BookDto createBook(String title, String author, Integer totalPages, Long categoryId, String notes, User user) {
        Book book = new Book(title, author, totalPages, user);
        book.setNotes(notes);

        // Assigner catégorie si fournie
        if (categoryId != null) {
            Category category = categoryRepository.findByIdAndUserId(categoryId, user.getId())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            book.setCategory(category);
        }

        Book saved = bookRepository.save(book);
        return new BookDto(saved);
    }

    public BookDto updateBook(Long bookId, String title, String author, Integer totalPages, Long categoryId, String notes, Long userId) {
        Book book = bookRepository.findByIdAndUserId(bookId, userId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        book.setTitle(title);
        book.setAuthor(author);
        book.setTotalPages(totalPages);
        book.setNotes(notes);

        // Gérer la catégorie
        if (categoryId != null) {
            Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            book.setCategory(category);
        } else {
            book.setCategory(null);
        }

        Book saved = bookRepository.save(book);
        return new BookDto(saved);
    }

    public BookDto updateProgress(Long bookId, Integer currentPage, Boolean isFinished, String notes, Long userId) {
        Book book = bookRepository.findByIdAndUserId(bookId, userId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        // Calculer les pages lues depuis la dernière mise à jour
        Integer oldCurrentPage = book.getCurrentPage();
        Integer pagesRead = currentPage - oldCurrentPage;

        book.setCurrentPage(currentPage);
        if (isFinished != null) {
            book.setIsFinished(isFinished);
            if (isFinished && currentPage < book.getTotalPages()) {
                book.setCurrentPage(book.getTotalPages()); // Marquer comme terminé = toutes les pages lues
            }
        }
        if (notes != null) {
            book.setNotes(notes);
        }

        // Créer une session de lecture si des pages ont été lues
        if (pagesRead > 0) {
            ReadingSession session = new ReadingSession(LocalDate.now(), pagesRead, book);
            readingSessionRepository.save(session);
        }

        Book saved = bookRepository.save(book);
        return new BookDto(saved);
    }

    public void deleteBook(Long bookId, Long userId) {
        Book book = bookRepository.findByIdAndUserId(bookId, userId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));
        bookRepository.delete(book);
    }
}

