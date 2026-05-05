package PersistenceLayer.Models;

import PersistenceLayer.Enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "loans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private User reader;

    @ManyToOne
    @JoinColumn(name = "workerToGive_id")
    private User workerToGive;

    @ManyToOne
    @JoinColumn(name = "workerToReturn_id")
    private User workerToReturn;

    @ManyToOne
    @JoinColumn(name = "workerToDelete_id")
    private User workerToDelete;

    private LocalDate loanDate;
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}

