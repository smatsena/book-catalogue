package za.co.catalogue.management.dto;

import lombok.Data;
import za.co.catalogue.management.model.BookType;

import javax.validation.constraints.*;
        import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookCreateRequest {
    @NotBlank @Size(max = 255)
    private String name;

    @NotBlank @Size(max = 255)
    private String author;

    @NotNull
    private LocalDate publishDate;

    @NotNull @DecimalMin(value = "0.00") @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @NotNull
    private BookType bookType;
}
