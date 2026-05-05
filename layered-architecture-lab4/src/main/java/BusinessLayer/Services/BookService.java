package BusinessLayer.Services;

import BusinessLayer.Exceptions.ResourceNotFoundException;
import PersistenceLayer.BookRepository;
import PersistenceLayer.Models.Book;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepository bookRepo;

    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Book create(Book book) { return bookRepo.save(book); }

    public List<Book> findAll() { return bookRepo.findAll(); }

    public Book findById(UUID id) {
        return bookRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Книгу не знайдено"));
    }

    public Book update(UUID id, Book details) {
        Book book = findById(id);
        book.setTitle(details.getTitle());
        book.setAuthor(details.getAuthor());
        book.setStatus(details.getStatus());
        return bookRepo.save(book);
    }

    public void delete(UUID id) {
        Book book = findById(id);
        bookRepo.delete(book);
    }

    public Page<Book> getBooks(String title, Pageable pageable) {
        if (title != null && !title.trim().isEmpty()) {
            return bookRepo.findByTitleContainingIgnoreCase(title.trim(), pageable);
        }
        return bookRepo.findAll(pageable);
    }
}