package BusinessLayer.Services;

import BusinessLayer.Exceptions.ResourceNotFoundException;
import PersistenceLayer.Models.User;
import PersistenceLayer.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User create(User user) {
        return userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findById(UUID id) {
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));
    }

    public User update(UUID id, User details) {
        User user = findById(id);
        user.setFullName(details.getFullName());
        user.setEmail(details.getEmail());
        user.setRole(details.getRole());
        return userRepo.save(user);
    }

    public void delete(UUID id) {
        userRepo.delete(findById(id));
    }
}