package Model.Player;

import Model.Game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martijn on 27-1-2016.
 *
 * This Strategy is the simplest strategy.
 */
public class NaiveStrategy implements Strategy {

    /**
     * The strategy's name.
     */
    private String name;

    /**
     * Constructs a new NaiveStrategy and initializes its name.
     */
    public NaiveStrategy() {
        name = "Naive-Computer";
    }

    /**
     * Returns the strategy's name.
     * @return The strategy's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Determines the move that is made on the board.
     * @param board The Board to be played on.
     * @param hand The hand of the ComputerPlayer that uses this Strategy.
     * @return The Move that is made.
     */
    @Override
    public Move determineMove(Board board, List<Tile> hand) {
        // get all possible moves of one Tile from the board
        Map<Location, List<Tile>> possibleMoves = board.getPossibleMoves();

        // check for every tile in the hand if it fits on one of the possible places from the possible moves
        for (Tile tile : hand) {
            for (Location loc : possibleMoves.keySet()) {
                for (Tile tile2 : possibleMoves.get(loc)) {

                    // create a new PutMove with the first match found. This means only one Tile will be placed,
                    // hence the "Naive" part.
                    if (tile.equals(tile2)) {
                        Map<Location, Tile> tiles = new HashMap<>();
                        tiles.put(loc, tile);
                        return new PutMove(tiles);
                    }
                }
            }
        }

        // if no PutMove can be made, make a TradeMove.
        return determineTradeMove(hand);
    }

    /**
     * Makes a TradeMove with the given hand. Trades the first Tile in the hand.
     * @param hand The hand that is traded from.
     * @return A TradeMove with the first Tile from the hand.
     */
    public TradeMove determineTradeMove(List<Tile> hand) {
        List<Tile> tile = new ArrayList<>();
        tile.add(hand.get(0));
        return new TradeMove(tile);
    }

}
