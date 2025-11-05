package za.co.catalogue.management.dto;

import lombok.Data;
import za.co.catalogue.management.model.BookType;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for creating a new book.
 * 
 * <p>This DTO represents the request payload for creating a book.
 * All fields are required and validated. The ISBN is not included
 * as it is automatically generated server-side.
 * 
 * @see za.co.catalogue.management.model.Book
 */
@Data
public class BookCreateRequest {
    /** Book name/title, required, max length 255 characters */
    @NotBlank @Size(max = 255)
    private String name;

    /** Author name, required, max length 255 characters */
    @NotBlank @Size(max = 255)
    private String author;

    /** Publication date, required */
    @NotNull
    private LocalDate publishDate;

    /** Book price, required, must be non-negative, max 8 digits before decimal, 2 after */
    @NotNull @DecimalMin(value = "0.00") @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    /** Book type (HARD_COVER, SOFT_COVER, or EBOOK), required */
    @NotNull
    private BookType bookType;
}
