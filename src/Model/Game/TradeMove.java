package Model.Game;

import java.util.List;

/**
 * Created by Martijn on 13-1-2016.
 */
public class TradeMove extends Move {

    List<Tile> move;

    public TradeMove(List<Tile> move) {
        this.move = move;
    }

    public List<Tile> getMove() {
        return move;
    }

}
