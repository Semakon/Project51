package Model.Game.Exceptions;

/**
 * This runtimeException is thrown when the positioning of a PutMove is invalid.
 *
 * Created by Martijn on 22-1-2016.
 */
public class InvalidPositioningRuntimeException extends RuntimeException {

    public InvalidPositioningRuntimeException() {
        super("Positioning is invalid.");
    }

}
