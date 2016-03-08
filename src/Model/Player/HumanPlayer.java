package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidAmountRuntimeException;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * This class represents a player controlled by an actual person.
 *
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

    /**
     * Determines what PutMove is made by using client input.
     * @param board The board the move is made on.
     * @return The PutMove the player (client) decides to make.
     */
    public PutMove determinePutMove(Board board) {
        PutMove move = null;

        //TODO: get user input to create new Map<Location, Tile>

        return move;
    }

    /**
     * Determines what TradeMove is made by using client input.
     * @param board The board the move is made on.
     * @return The TradeMove the player (client) decides to make.
     */
    public TradeMove determineTradeMove(Board board) {
        TradeMove move = null;

        //TODO: get user input to create a new List<Tile>

        return move;
    }

    /**
     * Determines the type of move the player makes through client input.
     * @param board the current board.
     * @return The Move the player (client) decides to make.
     */
    @Override
    public Move determineMove(Board board) {
        Move move = null;
        //TODO: user input: trade or put?
        // if put: move = determinePutMove(board);
        // if trade: move = determineTradeMove(board);
        return move;
    }



}
