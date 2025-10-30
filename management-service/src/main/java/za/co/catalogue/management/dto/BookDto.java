package za.co.catalogue.management.dto;

import java.sql.Date;

public class BookDto {
    private String isbn;
    private String name;
    private String  author;
    private Date publishDate;
    private Double price;
    private String bookType;

    public BookDto(String isbn, String name, String author, Double price) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.price = price;
    }
}
