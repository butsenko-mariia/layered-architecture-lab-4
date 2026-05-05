package PresentationLayer.DTOs;

import PersistenceLayer.Enums.LoanStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanDTO {
    private UUID id;
    private String bookTitle;
    private String readerName;
    private String workerToGiveName;
    private String workerToReturnName;
    private String workerToDeleteName;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private LoanStatus status;
}