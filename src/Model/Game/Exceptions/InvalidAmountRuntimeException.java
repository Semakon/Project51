package Model.Game.Exceptions;

/**
 * Created by Martijn on 12-1-2016.
 *
 * This runtimeException is thrown when an amount of something is not valid (too high or too low).
 */
public class InvalidAmountRuntimeException extends RuntimeException{

    public InvalidAmountRuntimeException(String message) {
        super(message);
    }

}
