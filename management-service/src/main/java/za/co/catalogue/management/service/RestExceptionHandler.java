package za.co.catalogue.management.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import za.co.catalogue.management.exception.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST API exceptions.
 * 
 * <p>This class provides centralized exception handling for all REST controllers.
 * It converts application exceptions into appropriate HTTP responses with standardized error format.
 * 
 * <p>Handled exceptions:
 * <ul>
 *   <li>NotFoundException - HTTP 404 (Not Found)</li>
 *   <li>ConflictException - HTTP 409 (Conflict)</li>
 *   <li>BadRequestException - HTTP 400 (Bad Request)</li>
 *   <li>MethodArgumentNotValidException - HTTP 400 (Bad Request) with validation details</li>
 * </ul>
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles NotFoundException exceptions.
     * 
     * @param ex the NotFoundException that was thrown
     * @return ResponseEntity with HTTP 404 status and error details
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("NOT_FOUND", ex.getMessage()));
    }

    /**
     * Handles ConflictException exceptions.
     * 
     * @param ex the ConflictException that was thrown
     * @return ResponseEntity with HTTP 409 status and error details
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("CONFLICT", ex.getMessage()));
    }

    /**
     * Handles BadRequestException exceptions.
     * 
     * @param ex the BadRequestException that was thrown
     * @return ResponseEntity with HTTP 400 status and error details
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("BAD_REQUEST", ex.getMessage()));
    }

    /**
     * Handles validation exceptions from request body validation.
     * 
     * <p>Extracts field-level validation errors and includes them in the response.
     * 
     * @param ex the MethodArgumentNotValidException that was thrown
     * @return ResponseEntity with HTTP 400 status, error details, and field-level validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new ApiError("VALIDATION_ERROR", "Request validation failed", fieldErrors));
    }
}