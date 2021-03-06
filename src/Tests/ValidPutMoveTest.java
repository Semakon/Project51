package Tests;

import Model.Game.*;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martijn on 19-1-2016.
 */
public class ValidPutMoveTest {

    public static void main(String[] args) {

        Board b = new Board();

        //in move
        Location loc = new Location(5, 2);
        Location loc2 = new Location(5, 3);
        Location loc3 = new Location(5, 4);
        Location loc4 = new Location(5, 6);

        //in field
        Location loc5 = new Location(5, 7);
        Location loc6 = new Location(4, 4);
        Location loc7 = new Location(6, 4);
        Location loc8 = new Location(3, 4);

        //in move
        Tile tile = new Tile(2);
        Tile tile2 = new Tile(8);
        Tile tile3 = new Tile(20);
        Tile tile4 = new Tile(32);

        //in field
        Tile tile5 = new Tile(14);
        Tile tile6 = new Tile(21);
        Tile tile7 = new Tile(22);
        Tile tile8 = new Tile(23);

        //in move
        Map<Location, Tile> m = new HashMap<>();
        m.put(loc, tile);
        m.put(loc2, tile2);
        m.put(loc3, tile3);
        m.put(loc4, tile4);

        PutMove move = new PutMove(m);

        //in field
        b.getField().put(loc5, tile5);
        b.getField().put(loc6, tile6);
        b.getField().put(loc7, tile7);
        b.getField().put(loc8, tile8);

        PutMoveValidator pmv = new PutMoveValidator(b, move);
        try {
            if (pmv.validMove()) {
                b.makePutMove(move);
                System.out.println("Move valid: true");
            }
        } catch (InvalidMoveException e) {
            System.out.println("Move invalid: " + e.getMessage());
        }
    }
}
