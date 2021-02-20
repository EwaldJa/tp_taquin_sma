package utils.exceptions;

public class NotEnoughMailsException extends RuntimeException {

    public NotEnoughMailsException(String message) {
        super(message);
    }
}
