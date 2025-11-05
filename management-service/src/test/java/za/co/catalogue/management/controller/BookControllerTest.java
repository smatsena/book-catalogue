package za.co.catalogue.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import za.co.catalogue.management.dto.*;
import za.co.catalogue.management.exception.BadRequestException;
import za.co.catalogue.management.exception.NotFoundException;
import za.co.catalogue.management.model.BookType;
import za.co.catalogue.management.service.BookService;
import za.co.catalogue.management.service.RestExceptionHandler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookController Unit Tests")
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BookResponse testBookResponse;
    private BookCreateRequest testCreateRequest;
    private BookUpdateRequest testUpdateRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // For LocalDate support
        
        testBookResponse = createTestBookResponse();
        testCreateRequest = createTestCreateRequest();
        testUpdateRequest = createTestUpdateRequest();
    }

    @Test
    @DisplayName("Should return all books when getAll is called")
    void should_returnAllBooks_when_getAllCalled() throws Exception {
        // Arrange
        BookResponse book1 = createTestBookResponse("ISBN1", "Book 1");
        BookResponse book2 = createTestBookResponse("ISBN2", "Book 2");
        List<BookResponse> books = Arrays.asList(book1, book2);
        when(bookService.getAll()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/api/books/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].isbn").value("ISBN1"))
                .andExpect(jsonPath("$[0].name").value("Book 1"))
                .andExpect(jsonPath("$[1].isbn").value("ISBN2"))
                .andExpect(jsonPath("$[1].name").value("Book 2"));

        verify(bookService, times(1)).getAll();
    }

    @Test
    @DisplayName("Should return book when getByIsbn is called with valid ISBN")
    void should_returnBook_when_getByIsbnCalledWithValidIsbn() throws Exception {
        // Arrange
        String isbn = "ISBN1234567890";
        when(bookService.getByIsbn(isbn)).thenReturn(testBookResponse);

        // Act & Assert
        mockMvc.perform(get("/api/books/")
                .param("isbn", isbn))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.name").value(testBookResponse.getName()))
                .andExpect(jsonPath("$.author").value(testBookResponse.getAuthor()));

        verify(bookService, times(1)).getByIsbn(isbn);
    }

    @Test
    @DisplayName("Should return 404 when getByIsbn is called with non-existent ISBN")
    void should_return404_when_getByIsbnCalledWithNonExistentIsbn() throws Exception {
        // Arrange
        String isbn = "NONEXISTENT";
        when(bookService.getByIsbn(isbn)).thenThrow(new NotFoundException("Book not found", isbn));

        // Act & Assert
        mockMvc.perform(get("/api/books/")
                .param("isbn", isbn))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getByIsbn(isbn);
    }

    @Test
    @DisplayName("Should return books by name when search is called with name parameter")
    void should_returnBooksByName_when_searchCalledWithNameParameter() throws Exception {
        // Arrange
        String name = "Test Book";
        List<BookResponse> books = Arrays.asList(testBookResponse);
        when(bookService.searchByName(name)).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/api/books/search")
                .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(bookService, times(1)).searchByName(name);
        verify(bookService, never()).searchByAuthor(anyString());
    }

    @Test
    @DisplayName("Should create book when POST is called with valid request")
    void should_createBook_when_postCalledWithValidRequest() throws Exception {
        // Arrange
        BookResponse createdBook = createTestBookResponse("NEWISBN123456", "Created Book");
        when(bookService.create(any(BookCreateRequest.class))).thenReturn(createdBook);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value("NEWISBN123456"))
                .andExpect(jsonPath("$.name").value("Created Book"));

        verify(bookService, times(1)).create(any(BookCreateRequest.class));
    }

    @Test
    @DisplayName("Should return 400 when POST is called with invalid request")
    void should_return400_when_postCalledWithInvalidRequest() throws Exception {
        // Arrange
        BookCreateRequest invalidRequest = new BookCreateRequest();
        // Missing required fields

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).create(any(BookCreateRequest.class));
    }

    @Test
    @DisplayName("Should update book when PATCH is called with valid ISBN and request")
    void should_updateBook_when_patchCalledWithValidIsbnAndRequest() throws Exception {
        // Arrange
        String isbn = "ISBN1234567890";
        BookResponse updatedBook = createTestBookResponse(isbn, "Updated Book");
        when(bookService.update(eq(isbn), any(BookUpdateRequest.class))).thenReturn(updatedBook);

        // Act & Assert
        mockMvc.perform(patch("/api/books/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.name").value("Updated Book"));

        verify(bookService, times(1)).update(eq(isbn), any(BookUpdateRequest.class));
    }

    @Test
    @DisplayName("Should return 404 when PATCH is called with non-existent ISBN")
    void should_return404_when_patchCalledWithNonExistentIsbn() throws Exception {
        // Arrange
        String isbn = "NONEXISTENT";
        when(bookService.update(eq(isbn), any(BookUpdateRequest.class)))
                .thenThrow(new NotFoundException("Book not found", isbn));

        // Act & Assert
        mockMvc.perform(patch("/api/books/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).update(eq(isbn), any(BookUpdateRequest.class));
    }

    @Test
    @DisplayName("Should delete book when DELETE is called with valid ISBN")
    void should_deleteBook_when_deleteCalledWithValidIsbn() throws Exception {
        // Arrange
        String isbn = "ISBN1234567890";
        doNothing().when(bookService).delete(isbn);

        // Act & Assert
        mockMvc.perform(delete("/api/books/{isbn}", isbn))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).delete(isbn);
    }

    @Test
    @DisplayName("Should return 404 when DELETE is called with non-existent ISBN")
    void should_return404_when_deleteCalledWithNonExistentIsbn() throws Exception {
        // Arrange
        String isbn = "NONEXISTENT";
        doThrow(new NotFoundException("Book not found", isbn)).when(bookService).delete(isbn);

        // Act & Assert
        mockMvc.perform(delete("/api/books/{isbn}", isbn))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).delete(isbn);
    }

    @Test
    @DisplayName("Should return ResponseEntity with CREATED status when create succeeds")
    void should_returnResponseEntityWithCreatedStatus_when_createSucceeds() {
        // Arrange
        BookResponse createdBook = createTestBookResponse("NEWISBN", "New Book");
        when(bookService.create(any(BookCreateRequest.class))).thenReturn(createdBook);

        // Act
        ResponseEntity<BookResponse> response = bookController.create(testCreateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("NEWISBN", response.getBody().getIsbn());
        verify(bookService, times(1)).create(testCreateRequest);
    }

    // Helper methods
    private BookResponse createTestBookResponse() {
        return createTestBookResponse("ISBN1234567890", "Test Book");
    }

    private BookResponse createTestBookResponse(String isbn, String name) {
        return new BookResponse(
                isbn,
                name,
                "Test Author",
                LocalDate.of(2020, 1, 1),
                new BigDecimal("29.99"),
                BookType.HARD_COVER
        );
    }

    private BookCreateRequest createTestCreateRequest() {
        BookCreateRequest request = new BookCreateRequest();
        request.setName("Test Book");
        request.setAuthor("Test Author");
        request.setPublishDate(LocalDate.of(2020, 1, 1));
        request.setPrice(new BigDecimal("29.99"));
        request.setBookType(BookType.HARD_COVER);
        return request;
    }

    private BookUpdateRequest createTestUpdateRequest() {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setName("Updated Book");
        request.setAuthor("Updated Author");
        request.setPublishDate(LocalDate.of(2021, 1, 1));
        request.setPrice(new BigDecimal("39.99"));
        request.setBookType(BookType.SOFT_COVER);
        return request;
    }
}