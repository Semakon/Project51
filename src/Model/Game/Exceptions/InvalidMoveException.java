package Model.Game.Exceptions;

/**
 * Created by Martijn on 20-1-2016.
 *
 * This exception is thrown when a move is invalid.
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException(String message) {
        super(message);
    }

}
