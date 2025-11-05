package com.catalogue.web.controller;

import com.catalogue.web.dto.*;
import com.catalogue.web.service.BookService;
import com.catalogue.web.util.BookMapper;
import com.catalogue.web.util.DateFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public String listBooks(Model model) {
        logger.debug("Listing all books");
        List<BookResponse> books = bookService.getAllBooks();
        
        // Format dates for display
        Map<String, String> formattedDates = books.stream()
                .filter(book -> book.getPublishDate() != null)
                .collect(Collectors.toMap(
                        BookResponse::getIsbn,
                        book -> DateFormatter.format(book.getPublishDate()),
                        (existing, replacement) -> existing
                ));
        
        model.addAttribute("books", books);
        model.addAttribute("formattedDates", formattedDates);
        return "books/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.debug("Showing create book form");
        model.addAttribute("bookForm", new BookForm());
        return "books/create";
    }

    @PostMapping
    public String createBook(@Valid @ModelAttribute("bookForm") BookForm bookForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors in create book form");
            return "books/create";
        }

        logger.debug("Creating book: {}", bookForm.getName());
        BookCreateRequest request = bookMapper.toCreateRequest(bookForm);
        BookResponse created = bookService.createBook(request);
        redirectAttributes.addFlashAttribute("success", "Book created successfully with ISBN: " + created.getIsbn());
        logger.info("Book created successfully with ISBN: {}", created.getIsbn());
        return "redirect:/books";
    }

    @GetMapping("/{isbn}/edit")
    public String showEditForm(@PathVariable String isbn, Model model) {
        logger.debug("Showing edit form for book with ISBN: {}", isbn);
        BookResponse book = bookService.getBookByIsbn(isbn);
        BookForm bookForm = bookMapper.toForm(book);
        model.addAttribute("bookForm", bookForm);
        return "books/edit";
    }

    @PostMapping("/{isbn}")
    public String updateBook(@PathVariable String isbn,
                            @Valid @ModelAttribute("bookForm") BookForm bookForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors in update book form");
            return "books/edit";
        }

        logger.debug("Updating book with ISBN: {}", isbn);
        BookUpdateRequest request = bookMapper.toUpdateRequest(bookForm);
        bookService.updateBook(isbn, request);
        redirectAttributes.addFlashAttribute("success", "Book updated successfully");
        logger.info("Book updated successfully with ISBN: {}", isbn);
        return "redirect:/books";
    }

    @PostMapping("/{isbn}/delete")
    public String deleteBook(@PathVariable String isbn, RedirectAttributes redirectAttributes) {
        logger.debug("Deleting book with ISBN: {}", isbn);
        bookService.deleteBook(isbn);
        redirectAttributes.addFlashAttribute("success", "Book deleted successfully");
        logger.info("Book deleted successfully with ISBN: {}", isbn);
        return "redirect:/books";
    }
}
