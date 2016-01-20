package Model.Game;

import Model.Game.Enumerations.Axis;
import Model.Game.Enumerations.Identity;
import Model.Game.Enumerations.Positioning;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public boolean validMove(Move move) throws InvalidMoveException {
        boolean valid;
        if (move instanceof PutMove) {
            valid = validPut((PutMove) move);
        } else
            valid = move instanceof TradeMove && validTrade((TradeMove) move);
        return valid;
    }

    private boolean validPut(PutMove move) throws InvalidMoveException {
        boolean validLine = false;
        boolean validOrthogonalLines = true;

        for (Location loc : move.getMove().keySet()) {
            for (Location loc2 : field.keySet()) {
                if (loc.isEqualTo(loc2)) {
                    throw new InvalidMoveException("Location already in use."); //return false;
                }
                //TODO: check if move is attached to anything in the field
            }
        }

        Location startPoint = new ArrayList<>(move.getMove().keySet()).get(0);

        if (move.getPositioning() == Positioning.vertical) {
            validLine = validLine(move, Axis.Y, startPoint, 1) &&
                    validLine(move, Axis.Y, startPoint, -1);

            for (Location loc : move.getMove().keySet()) {
                List<Tile> orthogonalLine = new ArrayList<>();
                orthogonalLine.addAll(orthogonalLine(move, Axis.Y, loc, loc, 1));
                orthogonalLine.addAll(orthogonalLine(move, Axis.Y, loc, loc, -1));
                if (orthogonalLine.size() > 1 && !validOrthogonalLine(orthogonalLine)) {
                    validOrthogonalLines = false;
                    break;
                }
            }
        } else if (move.getPositioning() == Positioning.horizontal) {
            validLine = validLine(move, Axis.X, startPoint, 1) &&
                    validLine(move, Axis.X, startPoint, -1);

            for (Location loc : move.getMove().keySet()) {
                List<Tile> orthogonalLine = new ArrayList<>();
                orthogonalLine.addAll(orthogonalLine(move, Axis.X, loc, loc, 1));
                orthogonalLine.addAll(orthogonalLine(move, Axis.X, loc, loc, -1));
                if (orthogonalLine.size() > 1 && !validOrthogonalLine(orthogonalLine)) {
                    validOrthogonalLines = false;
                    break;
                }
            }
        } else if (move.getPositioning() == Positioning.unspecified) {  //TODO: figure out if this is the best solution
            validLine = validLine(move, Axis.X, startPoint, 1) &&
                    validLine(move, Axis.X, startPoint, -1);
            validOrthogonalLines = validLine(move, Axis.Y, startPoint, 1) &&
                    validLine(move, Axis.Y, startPoint, -1);

        } else {
            //should not occur
            //TODO: throw runtimeException: invalid positioning
        }
        return validLine && validOrthogonalLines;
    }

    /**
     * Creates a List of Tiles from the field that form a line with the Tile that lies on startPoint. This method is recursive.
     * @param move PutMove with startPoint
     * @param axis Axis on which the line lies.
     * @param location Location of Tile that is checked.
     * @param startPoint Location of the original Tile from the move that is checked.
     * @param step Step the X or Y takes (1 or -1).
     * @return A List of Tiles that form a line with the Tile that lies on startPoint.
     */
    public List<Tile> orthogonalLine(PutMove move, Axis axis, Location location, Location startPoint, int step) {
        List<Tile> line = new ArrayList<>();
        if (!location.isEqualTo(startPoint)) {
            for (Location loc : field.keySet()) {
                if (loc.isEqualTo(location)) {
                    line.add(field.get(loc));
                    List<Tile> temp = axis == Axis.Y ? orthogonalLine(move, axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                            orthogonalLine(move, axis, new Location(location.getX(), location.getY() + step), startPoint, step);
                    line.addAll(temp);
                    break; //right location is found, further looping is unnecessary
                }
            }
        } else {
            if (step > 0) {
                line.add(move.getMove().get(startPoint));
            }
            List<Tile> temp = axis == Axis.Y ? orthogonalLine(move, axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                    orthogonalLine(move, axis, new Location(location.getX(), location.getY() + step), startPoint, step);
            line.addAll(temp);
        }
        return line;
    }

    /**
     * Checks whether a line is valid according to the game rules.
     * @param line Line to be checked.
     * @return True if the line is valid.
     */
    public boolean validOrthogonalLine(List<Tile> line) throws InvalidMoveException {
        boolean valid = true;
        boolean sameColor = true;
        boolean sameShape = true;

        for (Tile tile : line) {
            for (Tile tile2 : line) {
                if (tile != tile2 && tile.getColor() != tile2.getColor()) {
                    sameColor = false;
                } else if (tile != tile2 && tile.getShape() != tile2.getShape()) {
                    sameShape = false;
                }
            }
        }
        if (sameColor && !sameShape) {
            for (Tile tile :line) {
                for (Tile tile2 : line) {
                    if (tile != tile2 && tile.getShape() == tile2.getShape()) {
                        throw new InvalidMoveException("Line contains double(s).");
                    }
                }
            }
        } else if (!sameColor && sameShape) {
            for (Tile tile : line) {
                for (Tile tile2 : line) {
                    if (tile != tile2 && tile.getColor() == tile2.getColor()) {
                        throw new InvalidMoveException("Line contains double(s).");
                    }
                }
            }
        } else {
            valid = false;
        }
        return valid;
    }

    /**
     * Checks whether a move with a certain axis is valid on the field. This method only checks the horizontal or vertical line,
     * depending on the axis given. It's a recursive method that uses a given location and increases either the X or Y variable
     * (again, depending on the given axis) by the given step. The step variable is either 1 or -1.
     * @param move PutMove that needs to be checked.
     * @param axis Axis on which the line lies.
     * @param location Location of Tile that is checked.
     * @param step Step the X or Y takes (1 or -1).
     * @return True if the move does not violate any game rules on the line.
     */
    public boolean validLine(PutMove move, Axis axis, Location location, int step) throws InvalidMoveException { // step is either +1 or -1
        System.out.println("\nChecking: (" + location.getX() + ", " + location.getY() + ")");
        for (Location loc : move.getMove().keySet()) {
            if (loc.isEqualTo(location)) {
                System.out.println("in Move");
                if (axis == Axis.X) {
                    return validLine(move, axis, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                } else {
                    return validLine(move, axis, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                }
            }
        }

        for (Location loc : field.keySet()) {
            if (loc.isEqualTo(location)) {
                System.out.println("in Field");

                //same color, different shapes
                if (move.getIdentity() == Identity.color) {
                    for (Location loc2 : move.getMove().keySet()) {
                        if (field.get(loc).getColor() != move.getMove().get(loc2).getColor()) {
                            throw new InvalidMoveException("Color doesn't fit in row/column.");
                        }
                        if (field.get(loc).getShape() == move.getMove().get(loc2).getShape()) {
                            throw new InvalidMoveException("Shape is already in row/column.");
                        }
                    }
                    if (axis == Axis.X) {
                        return validLine(move, axis, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                    } else {
                        return validLine(move, axis, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                    }

                //same shape, different colors
                } else if (move.getIdentity() == Identity.shape) {
                    for (Location loc2 : move.getMove().keySet()) {
                        if (field.get(loc).getShape() != move.getMove().get(loc2).getShape()) {
                            throw new InvalidMoveException("Shape doesn't fit in row/column.");
                        }
                        if (field.get(loc).getColor() == move.getMove().get(loc2).getColor()) {
                            throw new InvalidMoveException("Color is already in row/column.");
                        }
                    }
                    if (axis == Axis.X) {
                        return validLine(move, axis, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                    } else {
                        return validLine(move, axis, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                    }

                //one block in move set
                } else if (move.getIdentity() == Identity.unspecified) {
                    for (Location loc2 : move.getMove().keySet()) {
                        if (field.get(loc).getColor() == move.getMove().get(loc2).getColor() &&
                                field.get(loc).getShape() == move.getMove().get(loc2).getShape()) {
                            throw new InvalidMoveException("Same tile in row/column.");
                        }
                        if (field.get(loc).getColor() != move.getMove().get(loc2).getColor() &&
                                field.get(loc).getShape() != move.getMove().get(loc2).getShape()) {
                            throw new InvalidMoveException("Tile shares no identity with surrounding tiles.");
                        }
                    }
                } else {
                    //should not occur
                    //TODO: throw runtimeException: invalid identity
                }

                break; // right location is found on the field, no need to continue loop
            }
        }
        System.out.println("Empty space");
        if (axis == Axis.X) {
            if (location.getX() < move.higherBound(axis).getX() && location.getX() > move.lowerBound(axis).getX()) {
                System.out.println("Gap in move");
                return validLine(move, axis, new Location(location.getX() + step, location.getY()), step);                // recursive
            }
        } else {
            if (location.getY() < move.higherBound(axis).getY() && location.getY() > move.lowerBound(axis).getY()) {
                System.out.println("Gap in move");
                return validLine(move, axis, new Location(location.getX(), location.getY() + step), step);                // recursive
            }
        }

        return true;
    }

    private boolean validTrade(TradeMove move) {
        //TODO: implement
        return false;
    }

    /**
     * Clears the field of all Tiles.
     */
    public void reset() {
        field.clear();
    }

}
