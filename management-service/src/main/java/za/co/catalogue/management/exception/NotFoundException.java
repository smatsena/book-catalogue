package za.co.catalogue.management.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * 
 * <p>This exception is typically thrown when:
 * <ul>
 *   <li>A book with the specified ISBN does not exist</li>
 *   <li>A requested resource ID is invalid or not found</li>
 * </ul>
 * 
 * <p>Results in HTTP 404 (Not Found) response.
 */
public class NotFoundException extends RuntimeException {
    /**
     * Constructs a new NotFoundException with the specified message and value.
     * 
     * @param message the error message (may contain format placeholders)
     * @param value the value that was not found (for formatting the message)
     */
    public NotFoundException(String message, String value) { 
        super(String.format(message, value)); 
    }
    
    /**
     * Constructs a new NotFoundException with the specified message.
     * 
     * @param message the error message
     */
    public NotFoundException(String message) { 
        super(message); 
    }
}
