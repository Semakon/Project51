package Model.Game.Exceptions;

/**
 * Created by Martijn on 30-1-2016.
 */
public class InsufficientTilesInPoolException extends Exception {

    public InsufficientTilesInPoolException() {
        super("The pool does not contain enough tiles.");
    }

}
