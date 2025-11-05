package za.co.catalogue.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import za.co.catalogue.management.model.BookType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookResponse {
    private String isbn;
    private String name;
    private String author;
    private LocalDate publishDate;
    private BigDecimal price;
    private BookType bookType;
}

