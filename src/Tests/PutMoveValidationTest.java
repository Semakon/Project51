package Tests;

import Model.Game.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martijn on 13-1-2016.
 */
public class PutMoveValidationTest {

    public static void main(String[] args) {
        PutMove m = new PutMove();

        Location loc = new Location(5, 2);
        Location loc2 = new Location(5, 3);
        Location loc3 = new Location(5, 4);
        Location loc4 = new Location(5, 6);

        Tile tile = new Tile(2);
        Tile tile2 = new Tile(8);
        Tile tile3 = new Tile(20);
        Tile tile4 = new Tile(32);

        m.getMove().put(loc, tile);
        m.getMove().put(loc2, tile2);
        m.getMove().put(loc3, tile3);
        m.getMove().put(loc4, tile4);

        System.out.println("Positioning: " + m.validPositioning());
        System.out.println("Identity: " + m.validIdentity());
    }

}
