package za.co.catalogue.management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.co.catalogue.management.dto.*;
import za.co.catalogue.management.service.BookService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing book catalogue operations.
 * 
 * <p>All endpoints require authentication. GET and POST operations are available
 * to both admin and worker roles, while PATCH and DELETE are restricted to admin role only.
 */
@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService service;

    /**
     * Constructs a new BookController with the specified service.
     * 
     * @param service the book service to use for business logic
     */
    public BookController(BookService service) { this.service = service; }

    /**
     * Retrieves all books in the catalogue.
     * 
     * @return list of all books as BookResponse objects
     */
    @GetMapping("/all")
    public List<BookResponse> getAll() {
        return service.getAll();
    }

    /**
     * Retrieves a book by its ISBN.
     * 
     * @param isbn the ISBN of the book to retrieve (required)
     * @return the book with the specified ISBN
     * @throws za.co.catalogue.management.exception.NotFoundException if no book exists with the given ISBN
     */
    @GetMapping("/")
    public BookResponse getByIsbn(@RequestParam String isbn) {
        return service.getByIsbn(isbn);
    }

    /**
     * Searches for books by name or author.
     * 
     * <p>If both name and author parameters are provided, name takes precedence.
     * If neither is provided, returns all books.
     * 
     * @param name optional book name to search for
     * @param author optional author name to search for
     * @return list of books matching the search criteria
     */
    @GetMapping("/search")
    public List<BookResponse> search(
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> author
    ) {
        if (name.isPresent()) return service.searchByName(name.get());
        if (author.isPresent()) return service.searchByAuthor(author.get());
        return service.getAll();
    }

    /**
     * Creates a new book in the catalogue.
     * 
     * <p>The ISBN is automatically generated server-side and cannot be provided by the client.
     * If a book with the same name, author, and publish date already exists,
     * the existing book is updated instead of creating a duplicate.
     * 
     * @param request the book creation request containing book details
     * @return ResponseEntity with status CREATED (201) and the created book
     * @throws za.co.catalogue.management.exception.ConflictException if unable to generate a unique ISBN
     */
    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookCreateRequest request) {
        BookResponse created = service.create(request); // ISBN generated server-side
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Updates an existing book by ISBN.
     * 
     * <p>Only provided fields in the update request will be modified.
     * The ISBN itself cannot be changed. This operation is restricted to admin role only.
     * 
     * @param isbn the ISBN of the book to update
     * @param patch the update request containing fields to modify
     * @return the updated book
     * @throws za.co.catalogue.management.exception.NotFoundException if no book exists with the given ISBN
     */
    @PatchMapping("/{isbn}")
    public BookResponse update(@PathVariable String isbn, @Valid @RequestBody BookUpdateRequest patch) {
        return service.update(isbn, patch);
    }

    /**
     * Deletes a book from the catalogue by ISBN.
     * 
     * <p>This operation is permanent and restricted to admin role only.
     * 
     * @param isbn the ISBN of the book to delete
     * @throws za.co.catalogue.management.exception.NotFoundException if no book exists with the given ISBN
     */
    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        service.delete(isbn);
    }
}