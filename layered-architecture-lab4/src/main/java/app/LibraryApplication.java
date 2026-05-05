package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"BusinessLayer", "PersistenceLayer", "PresentationLayer"})
@EnableJpaRepositories(basePackages = "PersistenceLayer")
@EntityScan(basePackages = "PersistenceLayer.Models")
public class LibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}
