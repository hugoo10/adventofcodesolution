package fr.kahlouch.advent.exception;

public class GenericException extends RuntimeException {
    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }
}
