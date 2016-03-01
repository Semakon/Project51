package Model.Game.Exceptions;

/**
 * Created by Martijn on 22-1-2016.
 *
 * This runtimeException is thrown when the identity of a PutMove is invalid.
 */
public class InvalidIdentityRuntimeException extends RuntimeException {

    public InvalidIdentityRuntimeException() {
        super("Identity is invalid.");
    }

}
