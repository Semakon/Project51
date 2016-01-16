package Model.Game;

import Model.Game.Enumerations.Direction;
import Model.Game.Enumerations.Identity;
import Model.Game.Enumerations.Positioning;

import java.util.ArrayList;
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

    public boolean fieldIsEmpty() {
        return field.isEmpty();
    }

    public boolean validMove(Move move) {
        boolean valid;
        if (move instanceof PutMove) {
            valid = validPut((PutMove) move);
        } else
            valid = move instanceof TradeMove && validTrade((TradeMove) move);
        return valid;
    }

    private boolean validPut(PutMove move) { //TODO: add exceptions
        boolean validLine = false;
        boolean validOrthogonalLines = false;

        Location startPoint = new ArrayList<>(move.getMove().keySet()).get(0);

        if (move.getPositioning() == Positioning.vertical) {
            validLine = validLine(move, Direction.Y, startPoint, 1) &&
                    validLine(move, Direction.Y, startPoint, - 1);
            //TODO: add validOrthogonalLines

        } else if (move.getPositioning() == Positioning.horizontal) {
            validLine = validLine(move, Direction.X, startPoint, 1) &&
                    validLine(move, Direction.X, startPoint, - 1);
            //TODO: add validOrthogonalLines

        } else if (move.getPositioning() == Positioning.unspecified) {
            validLine = validLine(move, Direction.X, startPoint, 1) &&
                    validLine(move, Direction.X, startPoint, - 1);
            //TODO: add validOrthogonalLines

        } else {
            //should not occur
            //TODO: throw runtimeException: invalid positioning
        }

        return validLine && validOrthogonalLines;
    }

    public boolean validLine(PutMove move, Direction direction, Location location, int step) { // step is either +1 or -1
        for (Location loc : move.getMove().keySet()) {
            if (loc.isEqualTo(location)) {
                if (direction == Direction.X) {
                    return validLine(move, direction, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                } else {
                    return validLine(move, direction, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                }
            }
        }

        for (Location loc : field.keySet()) {
            if (loc.isEqualTo(location)) {

                //same color, different shapes
                if (move.getIdentity() == Identity.color) {
                    for (Location loc2 : move.getMove().keySet()) {
                        if (field.get(loc).getColor() != move.getMove().get(loc2).getColor()) {
                            //TODO: throw exception: color differs from move set
                            return false;
                        }
                        if (field.get(loc).getShape() == move.getMove().get(loc2).getShape()) {
                            //TODO: throw exception: shape is already in row/column
                            return false;
                        }
                    }
                    if (direction == Direction.X) {
                        return validLine(move, direction, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                    } else {
                        return validLine(move, direction, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                    }

                //same shape, different colors
                } else if (move.getIdentity() == Identity.shape) {
                    for (Location loc2 : move.getMove().keySet()) {
                        if (field.get(loc).getShape() != move.getMove().get(loc2).getShape()) {
                            //TODO: throw exception: shape differs from move set
                            return false;
                        }
                        if (field.get(loc).getColor() == move.getMove().get(loc2).getColor()) {
                            //TODO: throw exception: color already in row/column
                            return false;
                        }
                    }
                    if (direction == Direction.X) {
                        return validLine(move, direction, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                    } else {
                        return validLine(move, direction, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                    }

                //one block in move set
                } else if (move.getIdentity() == Identity.unspecified) {
                    for (Location loc2 : move.getMove().keySet()) {
                        if (field.get(loc).getColor() == move.getMove().get(loc2).getColor() &&
                                field.get(loc).getShape() == move.getMove().get(loc2).getShape()) {
                            //TODO: throw exception: same tile
                            return false;
                        }
                        if (field.get(loc).getColor() != move.getMove().get(loc2).getColor() &&
                                field.get(loc).getShape() != move.getMove().get(loc2).getShape()) {
                            //TODO: throw exception: tile shares no identity with surrounding tiles
                            return false;
                        }
                    }
                } else {
                    //should not occur
                    //TODO: throw runtimeException: invalid identity
                }

                break; // right location is found on the field, no need to continue loop
            }
        }
        return true;
    }

    private boolean validTrade(TradeMove move) {
        //TODO: implement
        return false;
    }

}
