package za.co.catalogue.management.exception;

/**
 * Exception thrown when a request is malformed or contains invalid data.
 * 
 * <p>This exception is typically thrown when:
 * <ul>
 *   <li>Required parameters are missing or empty</li>
 *   <li>Request data fails validation</li>
 *   <li>Invalid input is provided</li>
 * </ul>
 * 
 * <p>Results in HTTP 400 (Bad Request) response.
 */
public class BadRequestException extends RuntimeException {
    /**
     * Constructs a new BadRequestException with the specified message.
     * 
     * @param message the error message
     * @param value the value that caused the error (currently unused, for future use)
     */
    public BadRequestException(String message, String value) { 
        super(message); 
    }
}