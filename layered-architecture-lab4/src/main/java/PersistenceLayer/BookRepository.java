package PersistenceLayer;

import PersistenceLayer.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}