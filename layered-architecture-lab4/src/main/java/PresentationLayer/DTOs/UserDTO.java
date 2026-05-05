package PresentationLayer.DTOs;

import PersistenceLayer.Enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private UUID id;
    private String fullName;
    private String email;
    private UserRole role;
}