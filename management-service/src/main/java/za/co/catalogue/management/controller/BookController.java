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

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService service;

    public BookController(BookService service) { this.service = service; }

    @GetMapping("/all")
    public List<BookResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/")
    public BookResponse getByIsbn(@RequestParam String isbn) {
        return service.getByIsbn(isbn);
    }

    @GetMapping("/search")
    public List<BookResponse> search(
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> author
    ) {
        if (name.isPresent()) return service.searchByName(name.get());
        if (author.isPresent()) return service.searchByAuthor(author.get());
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookCreateRequest request) {
        BookResponse created = service.create(request); // ISBN generated server-side
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{isbn}")
    public BookResponse update(@PathVariable String isbn, @Valid @RequestBody BookUpdateRequest patch) {
        return service.update(isbn, patch);
    }

    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        service.delete(isbn);
    }
}