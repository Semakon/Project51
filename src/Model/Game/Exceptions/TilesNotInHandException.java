package Model.Game.Exceptions;

/**
 * Created by Martijn on 21-1-2016.
 */
public class TilesNotInHandException extends InvalidMoveException {

    public TilesNotInHandException(String message) {
        super(message);
    }

}
