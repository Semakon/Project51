package Tests;

import Model.Game.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martijn on 13-1-2016.
 */
public class ValidLocationTest {

    public static void main(String[] args) {
        Location loc = new Location(5, 2);
        Location loc2 = new Location(5, 3);
        Location loc3 = new Location(5, 4);
        Location loc4 = new Location(4, 5);

        Tile tile = new Tile(5);

        Map<Location, Tile> move = new HashMap<>();
        move.put(loc, tile);
        move.put(loc2, tile);
        move.put(loc3, tile);
        move.put(loc4, tile);

        PutMove m = new PutMove();
        System.out.println(m.validPositioning(move));
    }

}
