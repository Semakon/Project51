package Model.Player;

import Model.Game.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martijn on 27-1-2016.
 */
public class NaiveStrategy implements Strategy {

    private String name;

    public NaiveStrategy() {
        name = "Naive-Computer";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Move determineMove(Board board, List<Tile> hand) {
        Move move = null;
        List<Tile> placeableTiles = new ArrayList<>();
        for (List<Tile> list : board.getPossibleMoves().values()) {
            placeableTiles.addAll(list);
        }
        for (Tile tile : hand) {
            for (Tile tile2 : placeableTiles) {
                if (tile.isEqualTo(tile2)) {
                    move = determinePutMove(board, hand);
                }
            }
        }
        if (move == null) move = determineTradeMove(board, hand);
        return move;
    }

    @Override
    public TradeMove determineTradeMove(Board board, List<Tile> hand) {
        return null;
    }


    @Override
    public PutMove determinePutMove(Board board, List<Tile> hand) {
        return null;
    }
}
