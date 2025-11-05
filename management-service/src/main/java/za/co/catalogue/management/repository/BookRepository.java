package za.co.catalogue.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import za.co.catalogue.management.model.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(@NonNull String isbn);

    Optional<Book> findFirstByName(@NonNull String name);
    Optional<Book> findFirstByAuthor(@NonNull String author);

    Optional<Book> findByNameAndAuthorAndPublishDate(String name, String author, LocalDate publishDate);
    boolean existsByNameAndAuthorAndPublishDate(String name, String author, LocalDate publishDate);

    List<Book> findAllByName(@NonNull String name);
    List<Book> findAllByAuthor(@NonNull String author);

    boolean existsByIsbn(@NonNull String isbn);
    void deleteByIsbn(@NonNull String isbn);
}
