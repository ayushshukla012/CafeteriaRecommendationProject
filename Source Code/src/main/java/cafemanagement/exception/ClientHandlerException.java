package cafemanagement.exception;

public class ClientHandlerException extends RuntimeException {
    public ClientHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}