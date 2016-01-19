package Tests;

import Model.Game.*;
import Model.Game.Enumerations.Direction;
import Model.Game.Enumerations.Positioning;

import java.util.ArrayList;

/**
 * Created by Martijn on 19-1-2016.
 */
public class ValidLineTest {

    public static void main(String[] args) {

        Board b = new Board();
        PutMove m = new PutMove();

        Location loc = new Location(5, 2);
        Location loc2 = new Location(5, 3);
        Location loc3 = new Location(5, 4);
        // Location(5, 5) is not used
        Location loc4 = new Location(5, 5);

        Location loc5 = new Location(5, 5);

        Tile tile = new Tile(2);
        Tile tile2 = new Tile(8);
        Tile tile3 = new Tile(20);
        Tile tile4 = new Tile(32);

        Tile tile5 = new Tile(14);

        m.getMove().put(loc, tile);
        m.getMove().put(loc2, tile2);
        m.getMove().put(loc3, tile3);
        m.getMove().put(loc4, tile4);

        b.getField().put(loc5, tile5);

        System.out.println("Positioning: " + m.validPositioning());
        System.out.println("Identity: " + m.validIdentity());

        if (m.validPositioning() && m.validIdentity()) {

            boolean validLine = false;
            Location startPoint = new ArrayList<>(m.getMove().keySet()).get(0);

            if (m.getPositioning() == Positioning.vertical) {
                validLine = b.validLine(m, Direction.Y, startPoint, 1) &&
                        b.validLine(m, Direction.Y, startPoint, - 1);
            } else if (m.getPositioning() == Positioning.horizontal) {
                validLine = b.validLine(m, Direction.X, startPoint, 1) &&
                        b.validLine(m, Direction.X, startPoint, - 1);
            } else if (m.getPositioning() == Positioning.unspecified) {
                validLine = b.validLine(m, Direction.X, startPoint, 1) &&
                        b.validLine(m, Direction.X, startPoint, - 1);
            } else {
                //should not occur
                System.out.println("invalid Positioning");
            }

            System.out.println("validLine: " + validLine);
        }

    }
}
