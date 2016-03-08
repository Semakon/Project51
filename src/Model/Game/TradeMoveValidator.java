package Model.Game;

import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * Created by Martijn on 30-1-2016.
 *
 * This class has a method to check whether a TradeMove is valid according to the game's rules.
 */
public class TradeMoveValidator {

    /**
     * The TradeMove that is validated.
     */
    private TradeMove move;

    /**
     * Constructs a new TradeMoveValidator with the parameter 'move' as TradeMove to be validated.
     * @param move The TradeMove that is to be validated.
     */
    public TradeMoveValidator(TradeMove move) {
        this.move = move;
    }

    /**
     * Checks whether the hand contains all Tiles from that move.
     * @param hand The player's hand.
     * @return True if all Tiles from the move are in hand.
     */
    public boolean correctHand(List<Tile> hand) throws InvalidMoveException {
        // Check whether hand contains all Tiles that are in the move.
        if (!hand.containsAll(move.getMove())) {
            throw new InvalidMoveException("Not all tiles from the move are in the hand.");
        }
        return true;
    }

}
