package Model.Game;

import java.util.List;

/**
 * Created by Martijn on 13-1-2016.
 *
 * This class represents a trade move. It contains a list of Tiles that forms the actual move. This class extends
 * the class Move so it can be used to generalize PutMoves and TradeMoves.
 */
public class TradeMove extends Move {

    /**
     * The actual list of Tiles that represent the move.
     */
    List<Tile> move;

    /**
     * Constructs a new TradeMove with a list of Tiles as the move.
     * @param move The move that is made.
     */
    public TradeMove(List<Tile> move) {
        this.move = move;
    }

    /**
     * Returns the actual trade move.
     * @return The actual trade move.
     */
    public List<Tile> getMove() {
        return move;
    }

}
