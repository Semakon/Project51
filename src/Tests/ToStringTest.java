package Tests;

import Model.Game.Board;
import Model.Game.Location;
import Model.Game.Tile;

/**
 * Created by Martijn on 21-1-2016.
 */
public class ToStringTest {

    public static void main(String[] args) {
        Board b = new Board();

        b.getField().put(new Location(0, 0), new Tile(31));
        b.getField().put(new Location(1, 0), new Tile(30));
        b.getField().put(new Location(2, 0), new Tile(32));
        b.getField().put(new Location(1, 2), new Tile(18));
        b.getField().put(new Location(1, 1), new Tile(24));
        b.getField().put(new Location(1, 3), new Tile(6));

        System.out.println(b.toString());
    }


}
