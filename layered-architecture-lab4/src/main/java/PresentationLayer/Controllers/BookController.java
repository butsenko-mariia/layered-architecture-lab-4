package PresentationLayer.Controllers;

import BusinessLayer.Services.LibraryService;
import PersistenceLayer.Models.Book;
import PersistenceLayer.Enums.BookStatus;
import PresentationLayer.DTOs.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final LibraryService libraryService;

    public BookController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public List<BookDTO> getAll() {
        return libraryService.getAllBooks() .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookDTO getById(@PathVariable UUID id) {
        return mapToDto(libraryService.getBookById(id));
    }

    @PutMapping("/{id}")
    public BookDTO update(@PathVariable UUID id, @RequestBody BookDTO dto, @RequestHeader("X-Worker-Id") UUID workerId) {
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .status(dto.getStatus())
                .build();
        return mapToDto(libraryService.updateBook(id, book, workerId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @RequestHeader("X-Worker-Id") UUID workerId) {
        libraryService.deleteBook(id, workerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookDTO dto, @RequestHeader("X-Worker-Id") UUID workerId) {
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .status(BookStatus.AVAILABLE)
                .build();
        return mapToDto(libraryService.addBook(book, workerId));
    }


    private BookDTO mapToDto(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .status(book.getStatus())
                .build();
    }

    @GetMapping("/page")
    public Page<BookDTO> getAllPaginated(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Book> bookPage = libraryService.getBooksPaginatedAndFiltered(title, page, size);

        return bookPage.map(this::mapToDto);
    }
}