package Model.Game.Exceptions;

/**
 * Created by Martijn on 22-1-2016.
 *
 * This runtimeException is thrown when the positioning of a PutMove is invalid.
 */
public class InvalidPositioningRuntimeException extends RuntimeException {

    public InvalidPositioningRuntimeException() {
        super("Positioning is invalid.");
    }

}
