package Model.Game.Exceptions;

/**
 * This runtimeException is thrown when an amount of something is not valid (too high or too low).
 *
 * Created by Martijn on 12-1-2016.
 */
public class InvalidAmountRuntimeException extends RuntimeException{

    public InvalidAmountRuntimeException(String message) {
        super(message);
    }

}
