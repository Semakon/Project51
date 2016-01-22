package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidMoveException;

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

    public PutMove determinePutMove(Board board) {

        return null;
    }

    public TradeMove determineTradeMove(Board board) {

        return null;
    }

    @Override
    public Move determineMove(Board board) {
        //TODO: implement
        //trade or put?
        //catch InvalidMoveException and InsufficientTilesInPoolException
        return null;
    }



}
