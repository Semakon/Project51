package Model.Game.Exceptions;

/**
 * Created by Martijn on 22-1-2016.
 */
public class InvalidPositioningRuntimeException extends RuntimeException {

    public InvalidPositioningRuntimeException() {
        super("Positioning is invalid.");
    }

    public InvalidPositioningRuntimeException(String message) {
        super(message);
    }

}
