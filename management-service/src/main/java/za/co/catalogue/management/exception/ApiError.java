package za.co.catalogue.management.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Standard error response structure for API errors.
 * 
 * <p>This class provides a consistent format for error responses across the API.
 * It includes an error code, message, optional details map, and a timestamp.
 */
public class ApiError {
    /** Error code identifying the type of error (e.g., "NOT_FOUND", "BAD_REQUEST") */
    public final String code;
    
    /** Human-readable error message */
    public final String message;
    
    /** Optional map containing additional error details (e.g., field validation errors) */
    public final Map<String, ?> details;
    
    /** Timestamp when the error occurred */
    public final Instant timestamp = Instant.now();

    /**
     * Constructs an ApiError with code and message only.
     * 
     * @param code the error code
     * @param message the error message
     */
    public ApiError(String code, String message) {
        this(code, message, null);
    }
    
    /**
     * Constructs an ApiError with code, message, and optional details.
     * 
     * @param code the error code
     * @param message the error message
     * @param details optional map containing additional error details
     */
    public ApiError(String code, String message, Map<String, ?> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }
}