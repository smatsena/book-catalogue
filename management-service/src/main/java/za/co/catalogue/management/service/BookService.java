package za.co.catalogue.management.service;

import za.co.catalogue.management.dto.*;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookResponse> getAll();
    BookResponse getByIsbn(String isbn);
    Optional<BookResponse> getFirstByAuthor(String author);
    Optional<BookResponse> getFirstByName(String name);
    List<BookResponse> searchByAuthor(String author);
    List<BookResponse> searchByName(String name);
    BookResponse create(BookCreateRequest request);              // ignores client ISBN
    BookResponse update(String isbn, BookUpdateRequest patch);   // ISBN immutable
    void delete(String isbn);
}
