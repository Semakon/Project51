package Model.Game;

import Model.Game.Enumerations.Axis;
import Model.Game.Enumerations.Identity;
import Model.Game.Enumerations.Positioning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Martijn on 13-1-2016.
 */
public class PutMove extends Move {

    private Identity identity = Identity.unspecified;
    private Positioning positioning = Positioning.unspecified;
    private Map<Location, Tile> move;

    /**
     * Creates a new instance of PutMove(). (to be continued)
     * @param move Move input by Player.
     */
    public PutMove(Map<Location, Tile> move) {
        this.move = move;
        if (validPositioning() && validIdentity()) {
            //TODO: implement
        }
    }

    public Map<Location, Tile> getMove() {
        return move;
    }

    public Identity getIdentity() {
        return identity;
    }

    public Positioning getPositioning() {
        return positioning;
    }

    /**
     * Gives the Location of the with the lowest X or Y depending on axis.
     * @param axis Determines whether the Location with the lowest X or Y is returned.
     * @return Location with lowest X/Y
     */
    public Location lowerBound(Axis axis) {
        List<Location> list = new ArrayList<>();
        for (Location loc : move.keySet()) {
            list.add(loc);
        }
        if (list.size() > 1) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (axis == Axis.X) {
                    if (list.get(i).getX() < list.get(i + 1).getX()) {
                        Location temp = list.get(i);
                        list.set(i, list.get(i + 1));
                        list.set(i + 1, temp);
                    }
                } else if (axis == Axis.Y) {
                    if (list.get(i).getY() < list.get(i + 1).getY()) {
                        Location temp = list.get(i);
                        list.set(i, list.get(i + 1));
                        list.set(i + 1, temp);
                    }
                }
            }
        }
        return list.get(list.size() - 1);
    }

    /**
     * Gives the Location of the with the highest X or Y depending on axis.
     * @param axis Determines whether the Location with the highest X or Y is returned.
     * @return Location with highest X/Y
     */
    public Location higherBound(Axis axis) {
        List<Location> list = new ArrayList<>();
        for (Location loc : move.keySet()) {
            list.add(loc);
        }
        if (list.size() > 1) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (axis == Axis.X) {
                    if (list.get(i).getX() > list.get(i + 1).getX()) {
                        Location temp = list.get(i);
                        list.set(i, list.get(i + 1));
                        list.set(i + 1, temp);
                    }
                } else if (axis == Axis.Y) {
                    if (list.get(i).getY() > list.get(i + 1).getY()) {
                        Location temp = list.get(i);
                        list.set(i, list.get(i + 1));
                        list.set(i + 1, temp);
                    }
                }
            }
        }
        return list.get(list.size() - 1);
    }

    /**
     * Checks whether a move is either all on one horizontal line or all on one vertical line and within Configuration.RANGE
     * distance from one another. Also checks whether the move set has duplicates.
     * @return True if the Location of the move is valid
     */
    public boolean validPositioning() {
        boolean verticalLine = true;
        boolean horizontalLine = true;
        boolean duplicate = false;
        boolean valid = true;

        outerLoop:
        for (Location loc : move.keySet()) {
            for (Location loc2 : move.keySet()) {
                if (loc.getX() != loc2.getX() || Math.abs(loc.getY() - loc2.getY()) > Configuration.RANGE - 1) {
                    verticalLine = false;
                    break outerLoop;
                }
            }
        }
        outerLoop:
        for (Location loc : move.keySet()) {
            for (Location loc2 : move.keySet()) {
                if (loc.getY() != loc2.getY() || Math.abs(loc.getX() - loc2.getX()) > Configuration.RANGE - 1) {
                    horizontalLine = false;
                    break outerLoop;
                }
            }
        }
        for (Location loc : move.keySet()) {
            for (Location loc2 : move.keySet()) {
                if (loc != loc2 && loc.isEqualTo(loc2)) {
                    duplicate = true;
                }
            }
        }
        if (move.size() == 1) {
            positioning = Positioning.unspecified;
        } else if (horizontalLine && !verticalLine) {
            positioning = Positioning.horizontal;
        } else if (!horizontalLine && verticalLine) {
            positioning = Positioning.vertical;
        } else {
            valid = false;
            positioning = Positioning.invalid;
        }

        return valid && !duplicate;
    }

    /**
     * Checks whether a move has all Tiles with the same color and different shape or same shape and different color.
     * @return True if the Tiles have valid shapes/colors
     */
    public boolean validIdentity() {
        boolean valid = true;
        boolean sameColor = true;
        boolean sameShape = true;

        for (Tile tile : move.values()) {
            for (Tile tile2 : move.values()) {
                if (tile != tile2 && tile.getColor() != tile2.getColor()) {
                    sameColor = false;
                }
                if (tile != tile2 && tile.getShape() != tile2.getShape()) {
                    sameShape = false;
                }
            }
        }

        if (move.size() == 1) {
            identity = Identity.unspecified;
        } else if (sameColor && !sameShape) {
            identity = Identity.color;
        } else if (!sameColor && sameShape) {
            identity = Identity.shape;
        } else {
            identity = Identity.invalid;
        }

        if (identity == Identity.color) {
            outerLoop:
            for (Tile tile : move.values()) {
                for (Tile tile2 : move.values()) {
                    if (tile != tile2 && tile.getShape() == tile2.getShape()) {
                        //m contains one or more doubles.
                        valid = false;
                        break outerLoop;
                    }
                }
            }
        } else if (identity == Identity.shape) {
            outerLoop:
            for (Tile tile : move.values()) {
                for (Tile tile2 : move.values()) {
                    if (tile != tile2 && tile.getColor() == tile2.getColor()) {
                        //m contains one or more doubles.
                        valid = false;
                        break outerLoop;
                    }
                }
            }
        } else {
            valid = identity == Identity.unspecified;
        }

        return valid;
    }

}
