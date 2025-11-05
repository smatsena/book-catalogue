package za.co.catalogue.management.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, String value) { super(message); }
    public NotFoundException(String message) { super(message); }
}
