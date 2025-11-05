package com.catalogue.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ModelAttribute("bookTypes")
    public com.catalogue.web.dto.BookType[] getBookTypes() {
        return com.catalogue.web.dto.BookType.values();
    }

    @ExceptionHandler(BookNotFoundException.class)
    public String handleBookNotFoundException(BookNotFoundException ex, Model model) {
        logger.warn("Book not found: {}", ex.getMessage());
        model.addAttribute("errorType", "404");
        return "error";
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public String handleServiceUnavailableException(ServiceUnavailableException ex, Model model) {
        logger.error("Service unavailable: {}", ex.getMessage(), ex);

        // Check if the cause is HttpHostConnectException
        Throwable cause = ex.getCause();
        if (cause != null && cause.getClass().getSimpleName().equals("HttpHostConnectException")) {
            model.addAttribute("errorType", "503"); // Use 503 for connection issues
            model.addAttribute("errorMessage", ex.getMessage());
        } else {
            model.addAttribute("errorType", "500"); // Use 500 for other service errors
            model.addAttribute("errorMessage", ex.getMessage());
        }

        return "error";
    }

    @ExceptionHandler(ApiException.class)
    public String handleApiException(ApiException ex, Model model, RedirectAttributes redirectAttributes) {
        logger.error("API error (status {}): {}", ex.getStatusCode(), ex.getMessage(), ex);
        
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
            model.addAttribute("errorType", "404");
            return "error";
        } else if (ex.getStatusCode() == HttpStatus.CONFLICT.value()) {
            redirectAttributes.addFlashAttribute("error", "A book with this information already exists");
            return "redirect:/books";
        } else if (ex.getStatusCode() >= 500) {
            model.addAttribute("errorType", "500");
            model.addAttribute("errorMessage", ex.getMessage());
            return "error";
        } else {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + ex.getMessage());
            return "redirect:/books";
        }
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, Model model) {
        logger.warn("HTTP method not supported: {}", ex.getMessage());
        model.addAttribute("errorType", "404");
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        logger.warn("Invalid argument: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "books/create";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        logger.error("Unexpected error occurred", ex);
        model.addAttribute("errorType", "500");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}

