package com.catalogue.web.service;

import com.catalogue.web.dto.BookCreateRequest;
import com.catalogue.web.dto.BookResponse;
import com.catalogue.web.dto.BookUpdateRequest;

import java.util.List;

public interface BookService {
    List<BookResponse> getAllBooks();
    BookResponse getBookByIsbn(String isbn);
    BookResponse createBook(BookCreateRequest request);
    BookResponse updateBook(String isbn, BookUpdateRequest request);
    void deleteBook(String isbn);
}

