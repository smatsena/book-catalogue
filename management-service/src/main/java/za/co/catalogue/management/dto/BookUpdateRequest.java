package za.co.catalogue.management.dto;

import lombok.Data;
import za.co.catalogue.management.model.BookType;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for updating an existing book.
 * 
 * <p>This DTO represents a partial update request (PATCH operation).
 * All fields are optional - only provided fields will be updated.
 * The ISBN cannot be changed and is not included in this DTO.
 * 
 * @see za.co.catalogue.management.model.Book
 */
@Data
public class BookUpdateRequest {
    /** Optional book name/title to update */
    private String name;
    
    /** Optional author name to update */
    private String author;
    
    /** Optional publication date to update */
    private LocalDate publishDate;
    
    /** Optional price to update */
    private BigDecimal price;
    
    /** Optional book type to update */
    private BookType bookType;
}

