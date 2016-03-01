package Model.Player;

import Model.Game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Location, List<Tile>> map = board.getPossibleMoves();
        for (Tile tile : hand) {
            for (Location loc : map.keySet()) {
                for (Tile tile2 : map.get(loc)) {
                    if (tile.equals(tile2)) {
                        Map<Location, Tile> tiles = new HashMap<>();
                        tiles.put(loc, tile);
                        return new PutMove(tiles);
                    }
                }
            }
        }
        return determineTradeMove(hand);
    }

    public TradeMove determineTradeMove(List<Tile> hand) {
        List<Tile> tile = new ArrayList<>();
        tile.add(hand.get(0));
        return new TradeMove(tile);
    }

}
