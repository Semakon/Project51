package Model.Game;

import Model.Game.Enumerations.Positioning;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martijn on 11-1-2016.
 */
public class Board {

    private Map<Location, Tile> field;

    public Board() {
        field = new HashMap<>();
    }

    public Map<Location, Tile> getField() {
        return field;
    }

    public boolean fieldIsEmpty() {
        return field.isEmpty();
    }

    public boolean validMove(Move move) {
        boolean valid;
        if (move instanceof PutMove) {
            valid = validPut((PutMove) move);
        } else
            valid = move instanceof TradeMove && validTrade((TradeMove) move); //subject to change
        return valid;
    }

    private boolean validPut(PutMove move) { //TODO: add exceptions
        Map<Location, Tile> m = move.move();

        if (move.getPositioning() == Positioning.vertical) {
            for (Location loc : m.keySet()) {
                //TODO: implement
            }
        }
        return false;
    }

    private boolean validTrade(TradeMove move) {
        //TODO: implement
        return false;
    }

}
