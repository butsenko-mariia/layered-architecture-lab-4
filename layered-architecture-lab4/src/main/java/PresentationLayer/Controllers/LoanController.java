package PresentationLayer.Controllers;

import BusinessLayer.Services.LibraryService;
import PersistenceLayer.Enums.UserRole;
import PersistenceLayer.Models.Book;
import PersistenceLayer.Models.Loan;
import PersistenceLayer.Models.User;
import PresentationLayer.DTOs.LoanRequestDTO;
import PresentationLayer.DTOs.LoanDTO;
import PresentationLayer.DTOs.LoanUpdateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LibraryService libraryService;

    public LoanController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    public ResponseEntity<LoanDTO> giveBook(@RequestBody LoanRequestDTO request, @RequestHeader("X-Worker-Id") UUID workerId) {
        Loan loan = libraryService.giveBook(request.getBookId(), request.getUserId(), workerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(loan));
    }

    @PutMapping("/{id}/return")
    public LoanDTO returnBook(@PathVariable UUID id, @RequestHeader("X-Worker-Id") UUID workerId) {
        Loan loan = libraryService.returnBook(id, workerId);
        return mapToDto(loan);
    }

    @GetMapping
    public List<LoanDTO> getAll(@RequestHeader("X-Worker-Id") UUID workerId) {
        return libraryService.getAllLoans(workerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LoanDTO getById(@PathVariable UUID id, @RequestHeader("X-Worker-Id") UUID searcherId) {
        return mapToDto(libraryService.getLoanById(id, searcherId));
    }

    @PutMapping("/{id}")
    public LoanDTO update(@PathVariable UUID id,
                          @RequestBody LoanUpdateDTO dto,
                          @RequestHeader("X-Worker-Id") UUID workerId) {

        User worker = libraryService.getUserById(workerId, workerId);

        Book book = libraryService.getBookById(dto.getBookId());
        User reader = libraryService.getUserById(dto.getReaderId(), dto.getReaderId());
        User workerToGive = libraryService.getUserById(dto.getWorkerToGiveId(), dto.getWorkerToGiveId());

        User workerToReturn = dto.getWorkerToReturnId() != null
                ?libraryService.getUserById(dto.getWorkerToReturnId(), dto.getWorkerToReturnId())
                : null;

        User workerToDelete = dto.getWorkerToDeleteId() != null
                ? libraryService.getUserById(dto.getWorkerToDeleteId(), dto.getWorkerToDeleteId())
                : null;

        Loan loan = Loan.builder()
                .book(book)
                .reader(reader)
                .workerToGive(workerToGive)
                .workerToReturn(workerToReturn)
                .workerToDelete(workerToDelete)
                .loanDate(dto.getLoanDate())
                .returnDate(dto.getReturnDate())
                .status(dto.getStatus())
                .build();

        return mapToDto(libraryService.updateLoan(id, loan, workerId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @RequestHeader("X-Worker-Id") UUID workerId) {
        libraryService.deleteLoan(id, workerId);
    }

    private LoanDTO mapToDto(Loan loan) {
        return LoanDTO.builder()
                .id(loan.getId())
                .bookTitle(loan.getBook().getTitle())
                .readerName(loan.getReader().getFullName())
                .workerToGiveName(loan.getWorkerToGive().getFullName())
                .workerToReturnName(loan.getWorkerToReturn() != null ? loan.getWorkerToReturn().getFullName() : null)
                .workerToDeleteName(loan.getWorkerToDelete() != null ? loan.getWorkerToDelete().getFullName() : null)
                .loanDate(loan.getLoanDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus())
                .build();
    }
}