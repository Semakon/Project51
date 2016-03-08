package Model.Game.Exceptions;

/**
 * This runtimeException is thrown when an ID is invalid in some way.
 *
 * Created by martijn on 11-1-16.
 */
public class InvalidIdRuntimeException extends RuntimeException {

    public InvalidIdRuntimeException(String message) {
        super(message);
    }

}
