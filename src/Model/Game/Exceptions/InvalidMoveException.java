package Model.Game.Exceptions;

/**
 * Created by Martijn on 20-1-2016.
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException() {
        super("Move is invalid.");
    }

    public InvalidMoveException(String message) {
        super(message);
    }

}