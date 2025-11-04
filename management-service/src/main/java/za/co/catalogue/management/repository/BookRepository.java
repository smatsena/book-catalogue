package za.co.catalogue.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.catalogue.management.dto.BookDto;
import za.co.catalogue.management.entity.Book;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     *
     * @param isbn
     * @return
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     *
     * @param title
     * @return
     */
    Optional<Book> findByName(@NotBlank String title);

    /**
     *
     * @param author
     * @return
     */
    Optional<Book> findByAuthor(@NotBlank String author);

    /**
     *
     * @param book
     */
    BookDto addBook(@NotBlank BookDto book);

    /**
     *
     * @param isbn
     */
    void removeBook(@NotBlank String isbn);
}
