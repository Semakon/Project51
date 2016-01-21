package Model.Player;

import Model.Game.Board;
import Model.Game.Exceptions.InvalidMoveException;
import Model.Game.Move;
import Model.Game.Tile;

import java.util.List;

/**
 * Created by Martijn on 20-1-2016.
 */
public class HumanPlayer extends Player {

    /**
     * Creates a new instance of Player with a name.
     * @param name the player's name.
     */
    public HumanPlayer(String name, List<Tile> hand) {
        super(name, hand);
    }

    @Override
    public Move determineMove(Board board) {
        //TODO: implement
        //trade or put?
        //catch InvalidMoveException and InsufficientTilesInPoolException
        return null;
    }

}
