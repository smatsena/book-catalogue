package com.catalogue.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@Data
public class BookForm {
    private String isbn;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotNull(message = "Publish date is required")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Publish date must be in dd/MM/yyyy format")
    private String publishDate;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer digits and 2 decimal places")
    private Double price;

    @NotBlank(message = "Book type is required")
    private String bookType;
}

