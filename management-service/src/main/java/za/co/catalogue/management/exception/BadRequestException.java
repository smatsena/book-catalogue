package za.co.catalogue.management.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message, String value) { super(message); }
}