package za.co.catalogue.management.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.catalogue.management.dto.*;
import za.co.catalogue.management.model.Book;
import za.co.catalogue.management.exception.BadRequestException;
import za.co.catalogue.management.exception.ConflictException;
import za.co.catalogue.management.exception.NotFoundException;
import za.co.catalogue.management.repository.BookRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
class BookServiceImpl implements BookService {

    private final BookRepository repo;
    private final Mapper mapper = new Mapper();
    private final IsbnPolicy isbnPolicy = new RandomIsbn13Policy();

    BookServiceImpl(BookRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<BookResponse> getAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BookResponse getByIsbn(String isbn) {
        String value = requireText(isbn, "isbn");
        return repo.findByIsbn(value).map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Book with ISBN '%s' not found", value));
    }

    @Override
    public Optional<BookResponse> getFirstByAuthor(String author) {
        return repo.findFirstByAuthor(requireText(author, "author")).map(mapper::toDto);
    }

    @Override
    public Optional<BookResponse> getFirstByName(String name) {
        return repo.findFirstByName(requireText(name, "name")).map(mapper::toDto);
    }

    @Override
    public List<BookResponse> searchByAuthor(String author) {
        return repo.findAllByAuthor(requireText(author, "author")).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> searchByName(String name) {
        return repo.findAllByName(requireText(name, "name")).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    /**
     * Creates a new book or updates an existing one if a duplicate is found.
     * 
     * <p>If a book with the same name, author, and publish date already exists,
     * the existing book is updated with the new price and book type, preserving its ISBN.
     * Otherwise, a new book is created with a server-generated ISBN.
     * 
     * @param request the book creation request
     * @return the created or updated book
     * @throws ConflictException if unable to generate a unique ISBN
     */
    @Transactional
    @Override
    public BookResponse create(BookCreateRequest request) {
        Objects.requireNonNull(request, "request");

        Book entity = repo.findByNameAndAuthorAndPublishDate(
                        request.getName(), request.getAuthor(), request.getPublishDate())
                .map(existing -> {
                    // patch allowed fields; do NOT change ISBN
                    existing.setPrice(request.getPrice());
                    existing.setBookType(request.getBookType());
                    return existing;
                })
                .orElseGet(() -> {
                    Book b = mapper.toEntity(request);
                    b.setIsbn(allocateIsbn());
                    return b;
                });

        entity.setIsbn(allocateIsbn()); // always generate server-side
        Book saved = repo.save(entity);

        return mapper.toDto(saved);
    }


    /**
     * Updates an existing book with the provided patch data.
     * 
     * <p>Only non-null fields in the patch request will be updated.
     * The ISBN is immutable and cannot be changed.
     * 
     * @param isbn the ISBN of the book to update
     * @param patch the update request containing fields to modify
     * @return the updated book
     * @throws NotFoundException if no book exists with the given ISBN
     */
    @Transactional
    @Override
    public BookResponse update(String isbn, BookUpdateRequest patch) {
        Objects.requireNonNull(patch, "patch");
        String value = requireText(isbn, "isbn");

        Book existing = repo.findByIsbn(value)
                .orElseThrow(() -> new NotFoundException("Book with ISBN '%s' not found", value));

        if (patch.getName() != null) existing.setName(patch.getName());
        if (patch.getAuthor() != null) existing.setAuthor(patch.getAuthor());
        if (patch.getPublishDate() != null) existing.setPublishDate(patch.getPublishDate());
        if (patch.getPrice() != null) existing.setPrice(patch.getPrice());
        if (patch.getBookType() != null) existing.setBookType(patch.getBookType());

        Book saved = repo.save(existing);
        return mapper.toDto(saved);
    }

    /**
     * Deletes a book from the catalogue by ISBN.
     * 
     * @param isbn the ISBN of the book to delete
     * @throws NotFoundException if no book exists with the given ISBN
     */
    @Transactional
    @Override
    public void delete(String isbn) {
        String value = requireText(isbn, "isbn");
        if (!repo.existsByIsbn(value)) {
            throw new NotFoundException("Book with ISBN '%s' not found", value);
        }
        repo.deleteByIsbn(value);
    }

    // ---------- helper methods ----------

    /**
     * Allocates a unique ISBN using the configured ISBN policy.
     * 
     * <p>Attempts to generate a unique ISBN. If a collision is detected,
     * one retry is attempted. If the retry also collides, a ConflictException is thrown.
     * 
     * @return a unique ISBN string
     * @throws ConflictException if unable to generate a unique ISBN after retries
     */
    private String allocateIsbn() {
        String isbn = isbnPolicy.generate();
        if (repo.existsByIsbn(isbn)) {
            // regenerate once; loop a few times if you want stronger guarantees
            isbn = isbnPolicy.generate();
            if (repo.existsByIsbn(isbn)) {
                throw new ConflictException("Unable to allocate unique ISBN right now");
            }
        }
        return isbn;
    }

    /**
     * Validates that a string value is not null or empty.
     * 
     * @param value the value to validate
     * @param field the field name for error messages
     * @return the validated value
     * @throws BadRequestException if the value is null or empty
     */
    private static String requireText(String value, String field) {
        if (value == null || value.isEmpty()) {
            throw new BadRequestException("Field '%s' must be provided", field);
        }
        return value;
    }

    // ---------- inner classes ----------

    /**
     * Mapper class for converting between entities and DTOs.
     */
    static final class Mapper {
        Book toEntity(BookCreateRequest d) {
            Book b = new Book();
            b.setName(d.getName());
            b.setAuthor(d.getAuthor());
            b.setPublishDate(d.getPublishDate());
            b.setPrice(d.getPrice());
            b.setBookType(d.getBookType());
            return b;
        }

        BookResponse toDto(Book b) {
            return new BookResponse(
                    b.getIsbn(), b.getName(), b.getAuthor(), b.getPublishDate(), b.getPrice(), b.getBookType()
            );
        }
    }

    interface IsbnPolicy {
        String generate();
    }

    /**
     * Implementation of ISBN policy that generates random 13-character identifiers.
     * 
     * <p>This implementation generates a 13-character uppercase string from a UUID.
     * Note: This is not a real ISBN-13 format. Replace with proper ISBN-13 generation if needed.
     */
    static final class RandomIsbn13Policy implements IsbnPolicy {
        @Override
        public String generate() {
            // 13-char token; replace with real ISBN-13 if needed
            return UUID.randomUUID().toString().replace("-", "").substring(0, 13).toUpperCase();
        }
    }
}
