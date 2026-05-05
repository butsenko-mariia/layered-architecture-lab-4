package PresentationLayer.DTOs;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {
    private UUID bookId;
    private UUID userId;
    private UUID workerToGiveId;
}