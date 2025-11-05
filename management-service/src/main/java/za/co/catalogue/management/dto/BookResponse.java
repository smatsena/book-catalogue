package za.co.catalogue.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import za.co.catalogue.management.model.BookType;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for book responses.
 * 
 * <p>This DTO represents a book entity in API responses.
 * It includes all book information including the server-generated ISBN.
 * 
 * @see za.co.catalogue.management.model.Book
 */
@Data
@AllArgsConstructor
public class BookResponse {
    /** Unique ISBN identifier (13 characters) */
    private String isbn;
    
    /** Book name/title */
    private String name;
    
    /** Author name */
    private String author;
    
    /** Publication date */
    private LocalDate publishDate;
    
    /** Book price */
    private BigDecimal price;
    
    /** Book type (HARD_COVER, SOFT_COVER, or EBOOK) */
    private BookType bookType;
}

