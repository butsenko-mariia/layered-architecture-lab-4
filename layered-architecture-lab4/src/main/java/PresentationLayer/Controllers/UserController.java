package PresentationLayer.Controllers;

import BusinessLayer.Services.LibraryService;
import BusinessLayer.Services.UserService;
import PersistenceLayer.Models.Book;
import PersistenceLayer.Models.User;
import PresentationLayer.DTOs.BookDTO;
import PresentationLayer.DTOs.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final LibraryService libraryService;

    public UserController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO register(@RequestBody UserDTO dto, @RequestHeader("X-Worker-Id") UUID workerId) {
        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
        return mapToDto(libraryService.addUser(user, workerId));
    }

    @GetMapping
    public List<UserDTO> getAll(@RequestHeader("X-Worker-Id") UUID workerId) {
        return libraryService.getAllUsers(workerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable UUID id, @RequestHeader("X-Worker-Id") UUID workerId) {
        return mapToDto(libraryService.getUserById(id, workerId));
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable UUID id, @RequestBody UserDTO dto, @RequestHeader("X-Worker-Id") UUID workerId) {
        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
        return mapToDto(libraryService.updateUser(id, user, workerId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @RequestHeader("X-Worker-Id") UUID workerId) {
        libraryService.deleteUser(id, workerId);
    }

    private UserDTO mapToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}