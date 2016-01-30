package Model.Game;

import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * Created by Martijn on 30-1-2016.
 */
public class TradeMoveValidator {

    private TradeMove move;

    public TradeMoveValidator(TradeMove move) {
        this.move = move;
    }

    /**
     * Checks whether the hand contains all Tiles from that move.
     * @param hand The player's hand.
     * @return True if all Tiles from the move are in hand.
     */
    public boolean correctHand(List<Tile> hand) throws InvalidMoveException {
        if (!hand.containsAll(move.getMove())) {
            throw new InvalidMoveException("Not all tiles from the move are in the hand.");
        }
        return true;
    }

}
