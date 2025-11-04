package za.co.catalogue.management.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BookDto {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be ≤ 255 characters")
    private String name;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author must be ≤ 255 characters")
    private String author;

    private String isbn;

    @NotNull(message = "Publish date is required: yyyy-MM-dd")
    private java.time.LocalDate publishDate;

    @NotNull(message = "Price (ZAR) is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Price must be ≥ 0.00")
    @Digits(integer = 8, fraction = 2, message = "Price must have max 8 digits and 2 decimals")
    private Double price;

    @NotBlank(message = "Book type is required")
    @Pattern(regexp = "Hard Cover|Soft Cover|eBook", message = "Book type must be Hard Cover, Soft Cover, or eBook")
    private String bookType;


    public BookDto(String isbn, String name, String author, Double price) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.price = price;
    }
}
