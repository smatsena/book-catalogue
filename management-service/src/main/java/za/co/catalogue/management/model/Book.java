package za.co.catalogue.management.model;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "book_data",
        uniqueConstraints = {
        @UniqueConstraint(name = "uk_books_isbn", columnNames = "isbn"),
        @UniqueConstraint(
                name = "uk_books_name_author_date",
                columnNames = {"name", "author", "publish_date"}
        )
    })
@Data
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 13)
    private String isbn;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private BookType bookType;
}