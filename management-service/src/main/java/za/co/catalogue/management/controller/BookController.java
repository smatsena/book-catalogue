package za.co.catalogue.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.catalogue.management.dto.BookDto;
import za.co.catalogue.management.service.BookService;

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

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@NotBlank @PathVariable String isbn) {
        BookDto book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BookDto> findByName(@NotBlank @PathVariable String name) {
        BookDto book = bookService.getBookByName(name);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<BookDto> findByAuthor(@NotBlank @PathVariable String author) {
        BookDto book = bookService.getBookByAuthor(author);
        return ResponseEntity.ok(book);
    }
}
