package za.co.catalogue.management.service;

import za.co.catalogue.management.dto.*;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for book catalogue operations.
 * 
 * <p>Defines the contract for business logic operations on books,
 * including CRUD operations and search functionality.
 */
public interface BookService {
    /**
     * Retrieves all books in the catalogue.
     * 
     * @return list of all books
     */
    List<BookResponse> getAll();
    
    /**
     * Retrieves a book by its ISBN.
     * 
     * @param isbn the ISBN of the book to retrieve
     * @return the book with the specified ISBN
     * @throws za.co.catalogue.management.exception.NotFoundException if no book exists with the given ISBN
     */
    BookResponse getByIsbn(String isbn);
    
    /**
     * Retrieves the first book found by the given author.
     * 
     * @param author the author name to search for
     * @return Optional containing the first book if found, empty otherwise
     */
    Optional<BookResponse> getFirstByAuthor(String author);
    
    /**
     * Retrieves the first book found with the given name.
     * 
     * @param name the book name to search for
     * @return Optional containing the first book if found, empty otherwise
     */
    Optional<BookResponse> getFirstByName(String name);
    
    /**
     * Searches for all books by the given author.
     * 
     * @param author the author name to search for
     * @return list of all books by the specified author
     */
    List<BookResponse> searchByAuthor(String author);
    
    /**
     * Searches for all books with the given name.
     * 
     * @param name the book name to search for
     * @return list of all books with the specified name
     */
    List<BookResponse> searchByName(String name);
    
    /**
     * Creates a new book in the catalogue.
     * 
     * <p>The ISBN is automatically generated server-side and any ISBN
     * provided in the request will be ignored.
     * 
     * @param request the book creation request
     * @return the created book with generated ISBN
     * @throws za.co.catalogue.management.exception.ConflictException if unable to generate a unique ISBN
     */
    BookResponse create(BookCreateRequest request);
    
    /**
     * Updates an existing book by ISBN.
     * 
     * <p>The ISBN itself is immutable and cannot be changed.
     * Only provided fields in the update request will be modified.
     * 
     * @param isbn the ISBN of the book to update
     * @param patch the update request containing fields to modify
     * @return the updated book
     * @throws za.co.catalogue.management.exception.NotFoundException if no book exists with the given ISBN
     */
    BookResponse update(String isbn, BookUpdateRequest patch);
    
    /**
     * Deletes a book from the catalogue by ISBN.
     * 
     * @param isbn the ISBN of the book to delete
     * @throws za.co.catalogue.management.exception.NotFoundException if no book exists with the given ISBN
     */
    void delete(String isbn);
}
