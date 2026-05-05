package PresentationLayer.DTOs;

import PersistenceLayer.Enums.LoanStatus;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanUpdateDTO {
    private UUID bookId;
    private UUID readerId;
    private UUID workerToGiveId;
    private UUID workerToReturnId;
    private UUID workerToDeleteId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private LoanStatus status;
}