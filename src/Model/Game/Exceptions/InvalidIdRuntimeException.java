package Model.Game.Exceptions;

/**
 * Created by martijn on 11-1-16.
 *
 * This runtimeException is thrown when an ID is invalid in some way.
 */
public class InvalidIdRuntimeException extends RuntimeException {

    public InvalidIdRuntimeException(String message) {
        super(message);
    }

}
