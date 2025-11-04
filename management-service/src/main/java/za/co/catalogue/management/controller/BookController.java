package za.co.catalogue.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.catalogue.management.dto.BookDto;
import za.co.catalogue.management.service.BookService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/isbn")
    public ResponseEntity<BookDto> findByIsbn(@NotBlank @RequestParam String isbn) {
        BookDto book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/name")
    public ResponseEntity<BookDto> findByName(@NotBlank @RequestParam String name) {
        BookDto book = bookService.getBookByName(name);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/author")
    public ResponseEntity<BookDto> findByAuthor(@NotBlank @RequestParam String author) {
        BookDto book = bookService.getBookByAuthor(author);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/add")
    public ResponseEntity<BookDto> findByAuthor(@NotBlank @RequestBody BookDto dto) {
        return ResponseEntity.status(201).body(bookService.addBook(dto));
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<BookDto> update(@PathVariable String isbn, @Valid @RequestBody BookDto dto) {
        dto.setIsbn(isbn);
        return ResponseEntity.ok(bookService.updateBook(dto));
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteByIsbn(@PathVariable String isbn) {
        bookService.removeBook(isbn);
        return ResponseEntity.noContent().build();
    }
}