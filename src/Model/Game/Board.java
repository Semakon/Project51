package Model.Game;

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

    public boolean validMove(Move move) {
        boolean valid;
        if (move instanceof PutMove) {
            valid = validPut((PutMove)move);
        } else if (move instanceof TradeMove) {
            valid = validTrade((TradeMove)move);
        } else {
            valid = false; //subject to change
        }
        return valid;
    }

    private boolean validPut(PutMove move) { //TODO: add exceptions (?)
        Map<Location, Tile> m = move.Move();
        boolean validLocation = validLocation(m);
        boolean validColor = validColor(m);
        boolean validShape = validShape(m);
        return validLocation && validColor && validShape;
    }

    private boolean validTrade(TradeMove move) {
        //TODO: implement
        return false;
    }

    /**
     * Checks whether a move is either all on one horizontal line or all on one vertical line and within Configuration.RANGE
     * distance from one another.
     * @param m Map with Location and Tile from the move
     * @return True if the Location of the move is valid
     */
    private boolean validLocation(Map<Location, Tile> m) { //change to public to test with Tests.ValidLocationTest
        boolean validX = true;
//        System.out.println("X");
        outerLoop:
        for (Location loc : m.keySet()) {
            for (Location loc2 : m.keySet()) {
//                System.out.println("loc: " + loc.getX() + " loc2: " + loc2.getX());
//                System.out.println("absolute value: " + Math.abs(loc.getY() - loc2.getY()));
                if (loc.getX() != loc2.getX() || Math.abs(loc.getY() - loc2.getY()) > Configuration.RANGE) {
                    validX = false;
                    break outerLoop;
                }
            }
        }
        boolean validY = true;
//        System.out.println("\nY");
        outerLoop2:
        for (Location loc : m.keySet()) {
            for (Location loc2 : m.keySet()) {
//                System.out.println("loc: " + loc.getY() + " loc2: " + loc2.getY());
//                System.out.println("absolute value: " + Math.abs(loc.getX() - loc2.getX()));
                if (loc.getY() != loc2.getY() || Math.abs(loc.getX() - loc2.getX()) > Configuration.RANGE) {
                    validY = false;
                    break outerLoop2;
                }
            }
        }
//        System.out.println("X: " + validX + " Y: " + validY);
        return validX || validY;
    }

    private boolean validColor(Map<Location, Tile> m) {
        //TODO: implement
        return false;
    }

    private boolean validShape(Map<Location, Tile> m) {
        //TODO: implement
        return false;
    }

}
