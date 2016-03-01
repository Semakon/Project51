package Model.Game.Exceptions;

/**
 * Created by Martijn on 30-1-2016.
 *
 * This exception is thrown when the pool is being drawn from, but there aren't enough tiles in it.
 */
public class InsufficientTilesInPoolException extends Exception {

    public InsufficientTilesInPoolException() {
        super("The pool does not contain enough tiles.");
    }

}
