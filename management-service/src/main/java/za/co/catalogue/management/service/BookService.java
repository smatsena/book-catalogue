package za.co.catalogue.management.service;

import org.springframework.stereotype.Service;
import za.co.catalogue.management.dto.BookDto;
import za.co.catalogue.management.entity.Book;
import za.co.catalogue.management.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookDto book;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDto>  getAllBooks()
    {
        List<Book> books = bookRepository.findAll();
        List<BookDto> bookDtos = new ArrayList<>();
        books.forEach(book -> {
            bookDtos.add(new BookDto(book.getIsbn(), book.getName(), book.getAuthor(), book.getPrice()));
            }
        );
        return bookDtos;
    }

    public BookDto getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new RuntimeException("Book not found"));
        return new BookDto(book.getIsbn(), book.getName(), book.getAuthor(), book.getPrice());
    }

    public BookDto getBookByAuthor(String author) {
        Book book = bookRepository.findByAuthor(author).orElseThrow(() -> new RuntimeException("Book not found"));
        return new BookDto(book.getIsbn(), book.getName(), book.getAuthor(), book.getPrice());
    }

    public BookDto getBookByName(String name) {
        Book book = bookRepository.findByName(name).orElseThrow(() -> new RuntimeException("Book not found"));
        return new BookDto(book.getIsbn(), book.getName(), book.getAuthor(), book.getPrice());
    }

    public BookDto addBook(BookDto book) {
        this.book = book;
        populateIsbn();
        return bookRepository.addBook(book);
    }

    private void populateIsbn() {
        String isbn = UUID.randomUUID().toString().substring(0, 13);
        this.book.setIsbn(isbn);
    }

    public BookDto updateBook(BookDto book) {
        return null;
    }

    public void removeBook(String isbn) {

    }
}
