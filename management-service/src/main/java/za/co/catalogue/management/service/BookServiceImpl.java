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
                .orElseThrow(() -> new NotFoundException("Book with ISBN '%s' not found" + value));
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

    @Transactional
    @Override
    public void delete(String isbn) {
        String value = requireText(isbn, "isbn");
        if (!repo.existsByIsbn(value)) {
            throw new NotFoundException("Book with ISBN '%s' not found", value);
        }
        repo.deleteByIsbn(value);
    }

    // ---------- helpers ----------

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

    private static String requireText(String value, String field) {
        if (value == null || value.isEmpty()) {
            throw new BadRequestException("Field '%s' must be provided", field);
        }
        return value;
    }

    // mapping
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

    static final class RandomIsbn13Policy implements IsbnPolicy {
        @Override
        public String generate() {
            // 13-char token; replace with real ISBN-13 if needed
            return UUID.randomUUID().toString().replace("-", "").substring(0, 13).toUpperCase();
        }
    }
}
