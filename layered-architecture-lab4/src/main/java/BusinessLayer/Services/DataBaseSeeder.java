package BusinessLayer.Services;

import PersistenceLayer.Enums.UserRole;
import PersistenceLayer.Models.User;
import PersistenceLayer.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataBaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataBaseSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {

            User admin = User.builder()
                    .fullName("Головний Адмін")
                    .email("admin@library.com")
                    .role(UserRole.ADMIN)
                    .build();

            User savedAdmin = userRepository.save(admin);

            System.out.println("\n=======================================================");
            System.out.println("DATABASE WAS EMPTY. DEFAULT ADMIN WAS CREATED!");
            System.out.println("ADMIN'S ID: " + savedAdmin.getId());
            System.out.println("=======================================================\n");
        } else {
            System.out.println("\n[SEEDER] Users already exist. Skip admin creation.\n");
        }
    }
}