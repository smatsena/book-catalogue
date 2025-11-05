package com.catalogue.web.util;

import com.catalogue.web.dto.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BookMapper {

    private static final String DEFAULT_AUTHOR = "Unknown";

    public BookCreateRequest toCreateRequest(BookForm form) {
        BookCreateRequest request = new BookCreateRequest();
        request.setName(form.getName());
        request.setAuthor(DEFAULT_AUTHOR); // Management service requires author, set default
        request.setPublishDate(DateFormatter.parse(form.getPublishDate()));
        request.setPrice(form.getPrice() != null ? BigDecimal.valueOf(form.getPrice()) : null);
        request.setBookType(form.getBookType());
        return request;
    }

    public BookUpdateRequest toUpdateRequest(BookForm form) {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setName(form.getName());
        request.setPublishDate(DateFormatter.parse(form.getPublishDate()));
        request.setPrice(form.getPrice() != null ? BigDecimal.valueOf(form.getPrice()) : null);
        request.setBookType(form.getBookType());
        return request;
    }

    public BookForm toForm(BookResponse book) {
        BookForm form = new BookForm();
        form.setIsbn(book.getIsbn());
        form.setName(book.getName());
        form.setPublishDate(DateFormatter.format(book.getPublishDate()));
        form.setPrice(book.getPrice() != null ? book.getPrice().doubleValue() : null);
        form.setBookType(book.getBookType());
        return form;
    }
}

