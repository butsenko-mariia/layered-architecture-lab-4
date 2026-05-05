package BusinessLayer.Exceptions;

public class SecurityAccessException extends RuntimeException {
    public SecurityAccessException(String message) {
        super(message);
    }
}
