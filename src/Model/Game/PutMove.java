package Model.Game;

import Model.Game.Enumerations.Identity;
import Model.Game.Enumerations.Positioning;
import Model.Game.Exceptions.InvalidMoveException;

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
    }

    public Map<Location, Tile> getMove() {
        return move;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setPositioning(Positioning positioning) {
        this.positioning = positioning;
    }

    public Positioning getPositioning() {
        return positioning;
    }

//    public boolean validMove(boolean firstMove) throws InvalidMoveException {
//        if (firstMove) {
//            boolean validFirstMove = false;
//            for (Location loc : move.keySet()) {
//                if (loc.isEqualTo(0, 0)) {
//                    validFirstMove = true;
//                }
//            }
//            if (!validFirstMove) {
//                throw new InvalidMoveException("First move does not have a Tile on Location (0, 0).");
//            }
//        }
//        if (validPositioning() && validIdentity()) {
//            return true;
//        } else {
//            throw new InvalidMoveException("Move is invalid regardless of field.");
//        }
//    }

//    /**
//     * Checks whether a move is either all on one horizontal line or all on one vertical line and within Configuration.RANGE
//     * distance from one another. Also checks whether the move set has duplicates.
//     * @return True if the Location of the move is valid
//     */
//    public boolean validPositioning() {
//        boolean verticalLine = true;
//        boolean horizontalLine = true;
//        boolean duplicate = false;
//        boolean valid = true;
//
//        outerLoop:
//        for (Location loc : move.keySet()) {
//            for (Location loc2 : move.keySet()) {
//                if (loc.getX() != loc2.getX() || Math.abs(loc.getY() - loc2.getY()) > Configuration.RANGE - 1) {
//                    verticalLine = false;
//                    break outerLoop;
//                }
//            }
//        }
//        outerLoop:
//        for (Location loc : move.keySet()) {
//            for (Location loc2 : move.keySet()) {
//                if (loc.getY() != loc2.getY() || Math.abs(loc.getX() - loc2.getX()) > Configuration.RANGE - 1) {
//                    horizontalLine = false;
//                    break outerLoop;
//                }
//            }
//        }
//        for (Location loc : move.keySet()) {
//            for (Location loc2 : move.keySet()) {
//                if (loc != loc2 && loc.isEqualTo(loc2)) {
//                    duplicate = true;
//                }
//            }
//        }
//        if (move.size() == 1) {
//            positioning = Positioning.unspecified;
//        } else if (horizontalLine && !verticalLine) {
//            positioning = Positioning.horizontal;
//        } else if (!horizontalLine && verticalLine) {
//            positioning = Positioning.vertical;
//        } else {
//            valid = false;
//            positioning = Positioning.invalid;
//        }
//
//        return valid && !duplicate;
//    }
//
//    /**
//     * Checks whether a move has all Tiles with the same color and different shape or same shape and different color.
//     * @return True if the Tiles have valid shapes/colors
//     */
//    public boolean validIdentity() {
//        boolean valid = true;
//        boolean sameColor = true;
//        boolean sameShape = true;
//
//        for (Tile tile : move.values()) {
//            for (Tile tile2 : move.values()) {
//                if (tile != tile2 && tile.getColor() != tile2.getColor()) {
//                    sameColor = false;
//                }
//                if (tile != tile2 && tile.getShape() != tile2.getShape()) {
//                    sameShape = false;
//                }
//            }
//        }
//
//        if (move.size() == 1) {
//            identity = Identity.unspecified;
//        } else if (sameColor && !sameShape) {
//            identity = Identity.color;
//        } else if (!sameColor && sameShape) {
//            identity = Identity.shape;
//        } else {
//            identity = Identity.invalid;
//        }
//
//        if (identity == Identity.color) {
//            outerLoop:
//            for (Tile tile : move.values()) {
//                for (Tile tile2 : move.values()) {
//                    if (tile != tile2 && tile.getShape() == tile2.getShape()) {
//                        //m contains one or more doubles.
//                        valid = false;
//                        break outerLoop;
//                    }
//                }
//            }
//        } else if (identity == Identity.shape) {
//            outerLoop:
//            for (Tile tile : move.values()) {
//                for (Tile tile2 : move.values()) {
//                    if (tile != tile2 && tile.getColor() == tile2.getColor()) {
//                        //m contains one or more doubles.
//                        valid = false;
//                        break outerLoop;
//                    }
//                }
//            }
//        } else {
//            valid = identity == Identity.unspecified;
//        }
//
//        return valid;
//    }

}
