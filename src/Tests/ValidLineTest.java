package Tests;

import Model.Game.*;
import Model.Game.Enumerations.Axis;
import Model.Game.Enumerations.Positioning;

import java.util.ArrayList;

/**
 * Created by Martijn on 19-1-2016.
 */
public class ValidLineTest {

    public static void main(String[] args) {

        Board b = new Board();
        PutMove m = new PutMove();

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
        m.getMove().put(loc, tile);
        m.getMove().put(loc2, tile2);
        m.getMove().put(loc3, tile3);
        m.getMove().put(loc4, tile4);

        //in field
        b.getField().put(loc5, tile5);

        b.getField().put(loc6, tile6);
        b.getField().put(loc7, tile7);
        b.getField().put(loc8, tile8);

        System.out.println("validPositioning: " + m.validPositioning());
        System.out.println("validIdentity: " + m.validIdentity());

        if (m.validPositioning() && m.validIdentity()) {

            System.out.println("\nvalidMove: " + b.validMove(m));

//            boolean validLine = false;
//            Location startPoint = new ArrayList<>(m.getMove().keySet()).get(0);
//
//            if (m.getPositioning() == Positioning.vertical) {
//                validLine = b.validLine(m, Axis.Y, startPoint, 1) &&
//                        b.validLine(m, Axis.Y, startPoint, - 1);
//            } else if (m.getPositioning() == Positioning.horizontal) {
//                validLine = b.validLine(m, Axis.X, startPoint, 1) &&
//                        b.validLine(m, Axis.X, startPoint, - 1);
//            } else if (m.getPositioning() == Positioning.unspecified) {
//                validLine = b.validLine(m, Axis.X, startPoint, 1) &&
//                        b.validLine(m, Axis.X, startPoint, - 1);
//            } else {
//                //should not occur
//                System.out.println("invalid Positioning");
//            }

//            System.out.println("validLine: " + validLine);
//
//            for (Location location : m.getMove().keySet()) {
//                System.out.println("\nOrthogonal line 1: " + b.orthogonalLine(m, Axis.Y, location, location, 1));
//                System.out.println("Orthogonal line 2: " + b.orthogonalLine(m, Axis.Y, location, location, -1));
//            }

        }

    }
}
