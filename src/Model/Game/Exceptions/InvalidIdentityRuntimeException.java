package Model.Game.Exceptions;

/**
 * This runtimeException is thrown when the identity of a PutMove is invalid.
 *
 * Created by Martijn on 22-1-2016.
 */
public class InvalidIdentityRuntimeException extends RuntimeException {

    public InvalidIdentityRuntimeException() {
        super("Identity is invalid.");
    }

}
