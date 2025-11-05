package za.co.catalogue.management.exception;

/**
 * Exception thrown when a request conflicts with the current state of the resource.
 * 
 * <p>This exception is typically thrown when:
 * <ul>
 *   <li>A unique identifier cannot be generated (e.g., ISBN collision)</li>
 *   <li>An operation would violate a unique constraint</li>
 *   <li>A resource is in a state that prevents the requested operation</li>
 * </ul>
 * 
 * <p>Results in HTTP 409 (Conflict) response.
 */
public class ConflictException extends RuntimeException {
    /**
     * Constructs a new ConflictException with the specified message.
     * 
     * @param message the error message describing the conflict
     */
    public ConflictException(String message) { 
        super(message); 
    }
}