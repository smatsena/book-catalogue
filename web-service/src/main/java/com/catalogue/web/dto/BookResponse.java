package com.catalogue.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BookResponse {
    private String isbn;
    private String name;
    private String author;
    private LocalDate publishDate;
    private BigDecimal price;
    private String bookType;
}

