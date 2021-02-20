package utils.exceptions;

public class MutexAlreadyReleasedException extends RuntimeException {

    public MutexAlreadyReleasedException(String message) {
        super(message);
    }
}
