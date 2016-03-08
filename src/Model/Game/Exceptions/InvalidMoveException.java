package Model.Game.Exceptions;

/**
 * This exception is thrown when a move is invalid.
 *
 * Created by Martijn on 20-1-2016.
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException() {
        super("The move is invalid.");
    }

    public InvalidMoveException(String message) {
        super(message);
    }

}
