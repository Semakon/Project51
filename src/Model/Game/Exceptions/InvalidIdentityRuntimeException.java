package Model.Game.Exceptions;

/**
 * Created by Martijn on 22-1-2016.
 */
public class InvalidIdentityRuntimeException extends RuntimeException {

    public InvalidIdentityRuntimeException() {
        super("Identity is invalid.");
    }

    public InvalidIdentityRuntimeException(String message) {
        super(message);
    }

}
