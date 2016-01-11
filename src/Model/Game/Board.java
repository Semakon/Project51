package Model.Game;

import java.util.HashMap;

/**
 * Created by Martijn on 11-1-2016.
 */
public class Board {

    private HashMap<Location, Tile> field;

    public Board() {
        field = new HashMap<>();
    }

    public HashMap<Location, Tile> getField() {
        return field;
    }

}
