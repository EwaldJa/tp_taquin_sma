package utils.exceptions;

public class NotSynchronizedMethodException extends RuntimeException {

    public NotSynchronizedMethodException(String message) {
        super(message);
    }
}
