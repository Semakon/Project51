package Model.Game;

import Model.Game.Enumerations.Criteria;
import Model.Game.Enumerations.Positioning;

import java.util.Map;

/**
 * Created by Martijn on 13-1-2016.
 */
public class PutMove extends Move {

    private Criteria criteria = Criteria.unspecified;
    private Positioning positioning = Positioning.unspecified;

    public Map<Location, Tile> move() {
        Map<Location, Tile> move = null;

        //TODO: implement (with exceptions)

        return move;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public Positioning getPositioning() {
        return positioning;
    }

    /**
     * Checks whether a move is either all on one horizontal line or all on one vertical line and within Configuration.RANGE
     * distance from one another. Also checks whether the move set has duplicates.
     * @param m Map with Location and Tile from the move
     * @return True if the Location of the move is valid
     */
    public boolean validMovePositioning(Map<Location, Tile> m) {
        boolean validX = true;
        outerLoop:
        for (Location loc : m.keySet()) {
            for (Location loc2 : m.keySet()) {
                if (loc.getX() != loc2.getX() || Math.abs(loc.getY() - loc2.getY()) > Configuration.RANGE) {
                    validX = false;
                    break outerLoop;
                }
            }
        }
        boolean validY = true;
        outerLoop2:
        for (Location loc : m.keySet()) {
            for (Location loc2 : m.keySet()) {
                if (loc.getY() != loc2.getY() || Math.abs(loc.getX() - loc2.getX()) > Configuration.RANGE) {
                    validY = false;
                    break outerLoop2;
                }
            }
        }
        boolean duplicate = false;
        for (Location loc : m.keySet()) {
            for (Location loc2 : m.keySet()) {
                if (loc != loc2 && loc.equalsPos(loc2)) {
                    duplicate = true;
                }
            }
        }
        if (validX && validY) {
            positioning = Positioning.unspecified;
        } else if (validY) {
            positioning = Positioning.horizontal;
        } else if (validX) {
            positioning = Positioning.vertical;
        } else {
            positioning = Positioning.invalid;
        }
        return (validX || validY) && !duplicate;
    }

    /**
     * Checks whether a move is either all different colors or all different shapes.
     * @param m Map with Location and Tile from the move
     * @return True if the Tiles have valid shapes/colors
     */
    public boolean validMoveCriteria(Map<Location, Tile> m) {
        boolean validColor = true;
        outerLoop:
        for (Tile tile : m.values()) {
            for (Tile tile2 : m.values()) {
                if (tile != tile2 && tile.getColor() == tile2.getColor()) {
                    validColor = false;
                    break outerLoop;
                }
            }
        }
        boolean validShape = true;
        outerLoop2:
        for (Tile tile : m.values()) {
            for (Tile tile2 : m.values()) {
                if (tile != tile2 && tile.getShape() == tile2.getShape()) {
                    validShape = false;
                    break outerLoop2;
                }
            }
        }
        if (validColor && validShape) {
            criteria = Criteria.unspecified;
        } else if (validColor) {
            criteria = Criteria.color;
        } else if (validShape) {
            criteria = Criteria.shape;
        } else {
            criteria = Criteria.invalid;
        }
        return validColor || validShape;
    }

}
