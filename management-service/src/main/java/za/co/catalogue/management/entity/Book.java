package za.co.catalogue.management.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Entity
@Table(name = "book_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String author;

    @NotBlank
    @Column(nullable = false, unique = true, length = 32)
    private String isbn;

    @NotBlank
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "publish_date", nullable = false)
    private Date publishDate;

    @NotBlank
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private Double price;

    @NotBlank
    @Column(name = "book_type", nullable = false)
    private String bookType;
}