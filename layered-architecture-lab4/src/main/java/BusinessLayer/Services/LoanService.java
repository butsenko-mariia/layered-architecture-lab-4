package BusinessLayer.Services;

import BusinessLayer.Exceptions.ResourceNotFoundException;
import PersistenceLayer.Enums.LoanStatus;
import PersistenceLayer.LoanRepository;
import PersistenceLayer.Models.Book;
import PersistenceLayer.Models.Loan;
import PersistenceLayer.Models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LoanService {
    private final LoanRepository loanRepo;

    public LoanService(LoanRepository loanRepo) {
        this.loanRepo = loanRepo;
    }

    public Loan create(Loan loan) { return loanRepo.save(loan); }

    public List<Loan> findAll() { return loanRepo.findAll(); }

    public Loan findById(UUID id) {
        return loanRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Запис видачі не знайдено"));
    }

    public void delete(UUID id, UUID workerId) {
        Loan loan = findById(id);
        loan.setStatus(LoanStatus.DELETED);
        loan.setWorkerToDelete(User.builder().id(workerId).build());
        loanRepo.save(loan);
    }

    public Loan update(UUID id, Loan details) {
        Loan loan = findById(id);
        loan.setLoanDate(details.getLoanDate());
        loan.setBook(details.getBook());
        loan.setReader(details.getReader());
        loan.setWorkerToGive(details.getWorkerToGive());
        loan.setWorkerToReturn(details.getWorkerToReturn());
        loan.setWorkerToDelete(details.getWorkerToDelete());
        loan.setReturnDate(details.getReturnDate());
        loan.setStatus(details.getStatus());
        return loanRepo.save(loan);
    }
}