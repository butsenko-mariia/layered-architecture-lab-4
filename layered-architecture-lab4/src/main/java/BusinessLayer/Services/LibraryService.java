package BusinessLayer.Services;

import BusinessLayer.Exceptions.InvalidDataException;
import BusinessLayer.Exceptions.LibraryBusinessException;
import BusinessLayer.Exceptions.SecurityAccessException;
import PersistenceLayer.Enums.BookStatus;
import PersistenceLayer.Enums.LoanStatus;
import PersistenceLayer.Enums.UserRole;
import PersistenceLayer.Models.Book;
import PersistenceLayer.Models.Loan;
import PersistenceLayer.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LibraryService {

    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;

    public LibraryService(BookService bookService, UserService userService, LoanService loanService) {
        this.bookService = bookService;
        this.userService = userService;
        this.loanService = loanService;
    }

    public Book addBook(Book book, UUID workerId) {
        User worker = userService.findById(workerId);
        if (book.getTitle() == null || book.getAuthor() == null) {
            throw new InvalidDataException("Book's info is invalid");
        }
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN && worker.getRole() != UserRole.LIBRARIAN) {
            throw new SecurityAccessException("Only admin or librarian can add books");
        }
        return bookService.create(book);
    }

    public void deleteBook(UUID bookId, UUID workerID) {
        Book book = bookService.findById(bookId);
        User worker = userService.findById(workerID);
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN && worker.getRole() != UserRole.LIBRARIAN) {
            throw new SecurityAccessException("Only admin or librarian can delete books");
        }
        bookService.delete(book.getId());
        System.out.println("[AUDIT LOG - " + LocalDateTime.now() + "] DELETE: Book with ID:" + book.getId() +
                " deleted by ADMIN with ID:" + worker.getId());

    }

    public Book getBookById(UUID id) {
        return bookService.findById(id);
    }


    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    public Book updateBook(UUID id, Book book, UUID workerID) {
        User worker = userService.findById(workerID);
        if  (book.getTitle() == null || book.getAuthor() == null) {
            throw new InvalidDataException("Book's info is invalid");
        }
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN && worker.getRole() != UserRole.LIBRARIAN) {
            throw new SecurityAccessException("Only admin or librarian can update books");
        }
        return bookService.update(id, book);
    }

    public Page<Book> getBooksPaginatedAndFiltered(String title, int page, int size) {
        if (page < 0) {
            throw new InvalidDataException("Номер сторінки не може бути меншим за 0");
        }
        if (size <= 0) {
            throw new InvalidDataException("Розмір сторінки має бути більшим за 0");
        }
        if (size > 100) {
            throw new InvalidDataException("Не можна запитувати більше 100 книг за один раз");
        }
        if (title != null && title.length() > 100) {
            throw new InvalidDataException("Назва книги для пошуку занадто довга");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        return bookService.getBooks(title, pageable);
    }

    public User addUser(User user, UUID workerID) {
        User worker = userService.findById(workerID);
        if (user.getEmail() == null ||  user.getFullName() == null){
            throw new InvalidDataException("Username, Id or Email address is invalid");
        }
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN && worker.getRole() != UserRole.LIBRARIAN) {
            throw new SecurityAccessException("Only admin or librarian can add users");
        }
        return userService.create(user);
    }

    public User getUserById(UUID id, UUID searcherId) {
        User searcher = userService.findById(searcherId);
        if (searcher.getRole() != UserRole.ADMIN && !searcher.getId().equals(id)) {
            throw new SecurityAccessException("Only admin or reader with the same id can search for user`s infp with that id");
        }
        return userService.findById(id);
    }

    public void deleteUser(UUID userId, UUID workerID) {
        User user = userService.findById(userId);
        User worker = userService.findById(workerID);
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN) {
            throw new SecurityAccessException("Only admin can delete users");
        }
        userService.delete(user.getId());
        System.out.println("[AUDIT LOG - " + LocalDateTime.now() + "] DELETE: User with ID:" + user.getId() +
                " deleted by ADMIN with ID:" + worker.getId());

    }

    public List<User> getAllUsers(UUID workerID) {
        User worker = userService.findById(workerID);
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN) {
            throw new SecurityAccessException("Only admin can get all users");
        }
        return userService.findAll();
    }

    public User updateUser(UUID id, User user, UUID workerID) {
        User worker = userService.findById(workerID);
        if  (user.getEmail() == null ||  user.getFullName() == null || user.getRole() == null) {
            throw new InvalidDataException("User's info is invalid");
        }
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN) {
            throw new SecurityAccessException("Only admin can update users");
        }
        return userService.update(id, user);
    }

    public Loan getLoanById(UUID id, UUID searcherId) {
        User searcher = userService.findById(searcherId);
        Loan loan  = loanService.findById(id);
        if (searcher.getRole() != null && searcher.getRole() != UserRole.ADMIN && searcher.getRole() != UserRole.LIBRARIAN
                && !loan.getReader().getId().equals(searcher.getId())) {
            throw new SecurityAccessException("Only admin, librarian or reader who have a loan can get that loan");
        }
        return loan;
    }


    public void deleteLoan(UUID loanId, UUID workerID) {
        Loan loan =  loanService.findById(loanId);
        User worker = userService.findById(workerID);
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN) {
            throw new SecurityAccessException("Only admin can mark loans delete");
        }
        loanService.delete(loan.getId(), workerID);
        System.out.println("[AUDIT LOG - " + LocalDateTime.now() + "] DELETE: Loan with ID:" + loan.getId() +
                " was marked deleted by ADMIN with ID:" + worker.getId());

    }

    public List<Loan> getAllLoans(UUID workerID) {
        User worker = userService.findById(workerID);
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN && worker.getRole() != UserRole.LIBRARIAN) {
            throw new SecurityAccessException("Only admin or librarian can get all loans");
        }
        return loanService.findAll();
    }

    public Loan updateLoan(UUID id, Loan loan, UUID workerID) {
        User worker = userService.findById(workerID);
        if  (loan.getLoanDate() == null || loan.getWorkerToGive() == null || loan.getReader() == null || loan.getReader().getId() == null || loan.getBook() == null || loan.getStatus() == null) {
            throw new InvalidDataException("Loan's info is invalid");
        }
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN) {
            throw new SecurityAccessException("Only admin can update loans");
        }
        return loanService.update(id, loan);
    }

    @Transactional
    public Loan giveBook(UUID bookId, UUID readerId, UUID workerId) {
        Book book = bookService.findById(bookId);
        User reader = userService.findById(readerId);
        User worker = userService.findById(workerId);

        if (!book.getStatus().equals(BookStatus.AVAILABLE)) {
            throw new LibraryBusinessException("Book '" + book.getTitle() + "' is gaven to another reader.");
        }
        if (worker.getRole() != null && worker.getRole() != UserRole.ADMIN && worker.getRole() != UserRole.LIBRARIAN) {
            throw new SecurityAccessException("Only admin or librarian can give books");
        }
        if (readerId.equals(workerId)) {
            throw new SecurityAccessException("Worker's id and reader's id are the same!");
        }

        book.setStatus(BookStatus.LOANED);
        bookService.update(book.getId(), book);

        Loan loan = Loan.builder()
                .book(book)
                .reader(reader)
                .workerToGive(worker)
                .loanDate(LocalDate.now())
                .status(LoanStatus.ACTIVE)
                .build();

        System.out.println("[AUDIT LOG - " + LocalDateTime.now() + "] GIVE: Book with ID:" + bookId +
                " gaven Reader with ID:" + readerId);

        return loanService.create(loan);
    }

    @Transactional
    public Loan returnBook(UUID loanId, UUID workerId) {
        Loan loan = loanService.findById(loanId);
        User worker = userService.findById(workerId);

        if  (worker.getRole() != null && worker.getRole() != UserRole.ADMIN && worker.getRole() != UserRole.LIBRARIAN) {
            throw new SecurityAccessException("Only admin or librarian can return books.");
        }
        if (loan.getStatus().equals(LoanStatus.RETURNED)) {
            throw new LibraryBusinessException("This book was returned already.");
        }

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());
        loan.setWorkerToReturn(worker);

        Book book = loan.getBook();
        book.setStatus(BookStatus.AVAILABLE);
        bookService.update(book.getId(), book);

        System.out.println("[AUDIT LOG - " + LocalDateTime.now() + "] RETURN: Book with ID:" + book.getId() +
                " was returned with loan Id :" + loanId);

        return loanService.update(loan.getId(), loan);
    }
}