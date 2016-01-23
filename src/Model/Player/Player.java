package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidAmountRuntimeException;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * Created by Martijn on 11-1-2016.
 */
public abstract class Player {

    private String name;
    private List<Tile> hand;

    /**
     * Creates a new instance of Player with a name and a hand, that contains exactly Configuration.HAND Tiles.
     * @param name The player's name.
     * @param hand The player's Tiles.
     */
    public Player(String name, List<Tile> hand) {
        this.name = name;
        if (hand.size() != Configuration.HAND) {
            throw new InvalidAmountRuntimeException("The player's hand must contain exactly " + Configuration.HAND + " Tiles.");
        }
        this.hand = hand;
    }

    /**
     * returns the player's name.
     */
    public String getName() {
        return name;
    }

    public List<Tile> getHand() {
        return hand;
    }

    public abstract PutMove determinePutMove(Board board);

    public abstract TradeMove determineTradeMove(Board board);

    /**
     * Determines the next move.
     * @param board the current board.
     * @return the player's choice.
     */
    public abstract Move determineMove(Board board) throws InvalidMoveException;

    /**
     * Makes a move on the board.
     * @param board the current board.
     */
    public Move makeMove(Board board) {
        Move move;
        try {
            move = determineMove(board);
            if (move instanceof PutMove) {
                board.makePutMove((PutMove) move);
            } else if (move instanceof TradeMove) {
                board.makeTradeMove((TradeMove) move, hand);
            }
        } catch (InvalidMoveException e) {
            move = null;
            e.getMessage(); //TODO: display message to View and try again.
        }
        return move;
    }

}
