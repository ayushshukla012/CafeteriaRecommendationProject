package cafemanagement.exception;

public class GracefulRecoveryException extends Exception {
    public GracefulRecoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}