package za.co.catalogue.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import za.co.catalogue.management.model.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entity operations.
 * 
 * <p>Extends JpaRepository to provide standard CRUD operations.
 * Additional query methods are defined for finding books by various criteria.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Finds a book by its ISBN.
     * 
     * @param isbn the ISBN to search for (must not be null)
     * @return Optional containing the book if found, empty otherwise
     */
    Optional<Book> findByIsbn(@NonNull String isbn);

    /**
     * Finds the first book with the given name.
     * 
     * @param name the book name to search for (must not be null)
     * @return Optional containing the first matching book if found, empty otherwise
     */
    Optional<Book> findFirstByName(@NonNull String name);
    
    /**
     * Finds the first book by the given author.
     * 
     * @param author the author name to search for (must not be null)
     * @return Optional containing the first matching book if found, empty otherwise
     */
    Optional<Book> findFirstByAuthor(@NonNull String author);

    /**
     * Finds a book by name, author, and publication date.
     * 
     * @param name the book name
     * @param author the author name
     * @param publishDate the publication date
     * @return Optional containing the book if found, empty otherwise
     */
    Optional<Book> findByNameAndAuthorAndPublishDate(String name, String author, LocalDate publishDate);
    
    /**
     * Checks if a book exists with the given name, author, and publication date.
     * 
     * @param name the book name
     * @param author the author name
     * @param publishDate the publication date
     * @return true if a book exists with these attributes, false otherwise
     */
    boolean existsByNameAndAuthorAndPublishDate(String name, String author, LocalDate publishDate);

    /**
     * Finds all books with the given name.
     * 
     * @param name the book name to search for (must not be null)
     * @return list of all books with the specified name
     */
    List<Book> findAllByName(@NonNull String name);
    
    /**
     * Finds all books by the given author.
     * 
     * @param author the author name to search for (must not be null)
     * @return list of all books by the specified author
     */
    List<Book> findAllByAuthor(@NonNull String author);

    /**
     * Checks if a book exists with the given ISBN.
     * 
     * @param isbn the ISBN to check (must not be null)
     * @return true if a book exists with this ISBN, false otherwise
     */
    boolean existsByIsbn(@NonNull String isbn);
    
    /**
     * Deletes a book by its ISBN.
     * 
     * @param isbn the ISBN of the book to delete (must not be null)
     */
    void deleteByIsbn(@NonNull String isbn);
}
