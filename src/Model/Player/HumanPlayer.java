package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidAmountRuntimeException;
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
        PutMove move = null;
        //TODO: get user input to create new Map<Location, Tile>
        return move;
    }

    public TradeMove determineTradeMove(Board board) {
        TradeMove move = null;
        //TODO: get user input to create a new List<Tile>
        return move;
    }

    @Override
    public Move determineMove(Board board) {
        Move move = null;
        //TODO: user input: trade or put?
        // if put: move = determinePutMove(board);
        // if trade: move = determineTradeMove(board);
        return move;
    }



}
