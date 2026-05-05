package PresentationLayer.DTOs;

import PersistenceLayer.Enums.BookStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDTO {
    private UUID id;
    private String title;
    private String author;
    private BookStatus status;
}