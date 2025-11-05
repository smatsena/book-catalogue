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
    public final String code;
    public final String message;
    public final Map<String, ?> details;
    public final Instant timestamp = Instant.now();

    public ApiError(String code, String message) {
        this(code, message, null);
    }
    public ApiError(String code, String message, Map<String, ?> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }
}