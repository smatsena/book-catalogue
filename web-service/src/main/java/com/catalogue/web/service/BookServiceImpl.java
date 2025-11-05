package com.catalogue.web.service;

import com.catalogue.web.dto.BookCreateRequest;
import com.catalogue.web.dto.BookResponse;
import com.catalogue.web.dto.BookUpdateRequest;
import com.catalogue.web.exception.ApiException;
import com.catalogue.web.exception.BookNotFoundException;
import com.catalogue.web.exception.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public BookServiceImpl(RestTemplate restTemplate, @Value("${management.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        logger.info("BookService initialized - will connect to management service at: {}", baseUrl);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        try {
            logger.debug("Fetching all books from {}", baseUrl + "/all");
            ResponseEntity<List<BookResponse>> response = restTemplate.exchange(
                    baseUrl + "/all",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BookResponse>>() {}
            );
            logger.info("Successfully fetched {} books", response.getBody() != null ? response.getBody().size() : 0);
            return response.getBody();
        } catch (ResourceAccessException e) {
            logger.error("Failed to connect to management service", e);
            throw new ServiceUnavailableException("Unable to connect to the management service", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error while fetching books: {}", e.getStatusCode(), e);
            throw new ApiException("Failed to fetch books", e.getStatusCode().value(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching books", e);
            throw new ServiceUnavailableException("An unexpected error occurred while fetching books", e);
        }
    }

    @Override
    public BookResponse getBookByIsbn(String isbn) {
        try {
            logger.debug("Fetching book with ISBN: {}", isbn);
            // Use UriComponentsBuilder for proper URL encoding
            String url = baseUrl + "/?isbn={isbn}";
            ResponseEntity<BookResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    BookResponse.class,
                    isbn
            );
            logger.info("Successfully fetched book with ISBN: {}", isbn);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Book not found with ISBN: {}", isbn);
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
        } catch (ResourceAccessException e) {
            logger.error("Failed to connect to management service", e);
            throw new ServiceUnavailableException("Unable to connect to the management service", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error while fetching book: {}", e.getStatusCode(), e);
            throw new ApiException("Failed to fetch book", e.getStatusCode().value(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching book", e);
            throw new ServiceUnavailableException("An unexpected error occurred while fetching book", e);
        }
    }

    @Override
    public BookResponse createBook(BookCreateRequest request) {
        try {
            logger.debug("Creating book: {}", request.getName());
            ResponseEntity<BookResponse> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    BookResponse.class
            );
            logger.info("Successfully created book with ISBN: {}", response.getBody() != null ? response.getBody().getIsbn() : "unknown");
            return response.getBody();
        } catch (HttpClientErrorException.Conflict e) {
            logger.warn("Conflict while creating book: {}", e.getMessage());
            throw new ApiException("A book with this information already exists", HttpStatus.CONFLICT.value(), e);
        } catch (HttpClientErrorException.BadRequest e) {
            logger.warn("Bad request while creating book: {}", e.getMessage());
            throw new ApiException("Invalid book data provided", HttpStatus.BAD_REQUEST.value(), e);
        } catch (ResourceAccessException e) {
            logger.error("Failed to connect to management service", e);
            throw new ServiceUnavailableException("Unable to connect to the management service", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error while creating book: {}", e.getStatusCode(), e);
            throw new ApiException("Failed to create book", e.getStatusCode().value(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating book", e);
            throw new ServiceUnavailableException("An unexpected error occurred while creating book", e);
        }
    }

    @Override
    public BookResponse updateBook(String isbn, BookUpdateRequest request) {
        try {
            logger.debug("Updating book with ISBN: {}", isbn);
            ResponseEntity<BookResponse> response = restTemplate.exchange(
                    baseUrl + "/" + isbn,
                    HttpMethod.PATCH,
                    new HttpEntity<>(request),
                    BookResponse.class
            );
            logger.info("Successfully updated book with ISBN: {}", isbn);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Book not found for update with ISBN: {}", isbn);
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
        } catch (HttpClientErrorException.BadRequest e) {
            logger.warn("Bad request while updating book: {}", e.getMessage());
            throw new ApiException("Invalid book data provided", HttpStatus.BAD_REQUEST.value(), e);
        } catch (ResourceAccessException e) {
            logger.error("Failed to connect to management service", e);
            throw new ServiceUnavailableException("Unable to connect to the management service", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error while updating book: {}", e.getStatusCode(), e);
            throw new ApiException("Failed to update book", e.getStatusCode().value(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while updating book", e);
            throw new ServiceUnavailableException("An unexpected error occurred while updating book", e);
        }
    }

    @Override
    public void deleteBook(String isbn) {
        try {
            logger.debug("Deleting book with ISBN: {}", isbn);
            restTemplate.exchange(
                    baseUrl + "/" + isbn,
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );
            logger.info("Successfully deleted book with ISBN: {}", isbn);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Book not found for deletion with ISBN: {}", isbn);
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
        } catch (ResourceAccessException e) {
            logger.error("Failed to connect to management service", e);
            throw new ServiceUnavailableException("Unable to connect to the management service", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error while deleting book: {}", e.getStatusCode(), e);
            throw new ApiException("Failed to delete book", e.getStatusCode().value(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while deleting book", e);
            throw new ServiceUnavailableException("An unexpected error occurred while deleting book", e);
        }
    }
}
