package utils.exceptions;

public class EmptyMailboxException extends RuntimeException {

    public EmptyMailboxException(String message) {
        super(message);
    }
}
