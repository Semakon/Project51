package Model.Game;

import Model.Game.Enumerations.Axis;
import Model.Game.Enumerations.Identity;
import Model.Game.Enumerations.Positioning;
import Model.Game.Exceptions.InvalidIdentityRuntimeException;
import Model.Game.Exceptions.InvalidMoveException;
import Model.Game.Exceptions.InvalidPositioningRuntimeException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martijn on 30-1-2016.
 */
public class PutMoveValidator {

    private Board board;
    private PutMove move;
    private boolean firstMove = false;

    /**
     * Creates a new instance of PutMoveValidator. This class is used to validate a PutMove.
     * @param board The board on which the move must be validated.
     * @param move The move to be validated.
     */
    public PutMoveValidator(Board board, PutMove move) {
        this.board = board;
        this.move = move;
        if (board.getField().isEmpty()) firstMove = true;
    }

    public boolean validMove() throws InvalidMoveException {
        boolean valid;
        if (firstMove) {
            boolean validFirstMove = false;
            for (Location loc : move.getMove().keySet()) {
                if (loc.isEqualTo(0, 0)) {
                    validFirstMove = true;
                }
            }
            if (!validFirstMove) {
                throw new InvalidMoveException("First move does not have a Tile on Location (0, 0).");
            }
        }
        if (validPositioning() && validIdentity()) {
            valid = true;
        } else {
            throw new InvalidMoveException("Move is invalid regardless of field.");
        }
        if (!validPut()) {
            valid = false;
        }
        return valid;
    }

    /**
     * Checks whether a PutMove is valid. A PutMove is invalid if all Tiles in a line have the same color and a different shape
     * or all the same shape and different shape. The same Tile in a line is not allowed and the move must be connected to the field,
     * but may not override it.
     * @return True if the move is valid on this board.
     * @throws InvalidMoveException If the move is invalid.
     */
    private boolean validPut() throws InvalidMoveException {
        boolean validLine;
        boolean validOrthogonalLines = true;
        boolean touchesField = false;

        if (!firstMove) {
            for (Location loc : move.getMove().keySet()) {
                for (Location loc2 : board.getField().keySet()) {
                    if (loc.isEqualTo(loc2)) {
                        throw new InvalidMoveException("Location already in use.");
                    }
                    if (!touchesField) {
                        if (loc.isEqualTo(loc2.getX(), loc2.getY() + 1) || loc.isEqualTo(loc2.getX(), loc2.getY() - 1) || loc.isEqualTo(loc2.getX() + 1, loc2.getY()) ||
                                loc.isEqualTo(loc2.getX() - 1, loc2.getY())) {
                            touchesField = true;
                        }
                    }
                }
            }
            if (!touchesField) {
                throw new InvalidMoveException("Tiles are not connected to any other Tiles in the field.");
            }
        }


        Location startPoint = new ArrayList<>(move.getMove().keySet()).get(0);

        if (move.getPositioning() == Positioning.vertical) {
            validLine = validLine(Axis.Y, startPoint, 1) &&
                    validLine(Axis.Y, startPoint, -1);

            for (Location loc : move.getMove().keySet()) {
                List<Tile> orthogonalLine = new ArrayList<>();
                orthogonalLine.addAll(orthogonalLine(Axis.Y, loc, loc, 1));
                orthogonalLine.addAll(orthogonalLine(Axis.Y, loc, loc, -1));
                if (orthogonalLine.size() > 1 && !validLine(orthogonalLine)) {
                    validOrthogonalLines = false;
                    break;
                }
            }
        } else if (move.getPositioning() == Positioning.horizontal) {
            validLine = validLine(Axis.X, startPoint, 1) &&
                    validLine(Axis.X, startPoint, -1);

            for (Location loc : move.getMove().keySet()) {
                List<Tile> orthogonalLine = new ArrayList<>();
                orthogonalLine.addAll(orthogonalLine(Axis.X, loc, loc, 1));
                orthogonalLine.addAll(orthogonalLine(Axis.X, loc, loc, -1));
                if (orthogonalLine.size() > 1 && !validLine(orthogonalLine)) {
                    validOrthogonalLines = false;
                    break;
                }
            }
        } else if (move.getPositioning() == Positioning.unspecified) {  //TODO: figure out if this is the best solution
            validLine = validLine(Axis.X, startPoint, 1) &&
                    validLine(Axis.X, startPoint, -1);
            validOrthogonalLines = validLine(Axis.Y, startPoint, 1) &&
                    validLine(Axis.Y, startPoint, -1);

        } else {
            //should not occur
            throw new InvalidPositioningRuntimeException();
        }
        return validLine && validOrthogonalLines;
    }

    /**
     * Checks whether a move is either all on one horizontal line or all on one vertical line and within Configuration.RANGE
     * distance from one another. Also checks whether the move set has duplicates.
     * @return True if the Location of the move is valid
     */
    private boolean validPositioning() throws InvalidMoveException {
        boolean verticalLine = true;
        boolean horizontalLine = true;

        outerLoop:
        for (Location loc : move.getMove().keySet()) {
            for (Location loc2 : move.getMove().keySet()) {
                if (loc.getX() != loc2.getX() || Math.abs(loc.getY() - loc2.getY()) > Configuration.RANGE - 1) {
                    verticalLine = false;
                    break outerLoop;
                }
            }
        }
        outerLoop:
        for (Location loc : move.getMove().keySet()) {
            for (Location loc2 : move.getMove().keySet()) {
                if (loc.getY() != loc2.getY() || Math.abs(loc.getX() - loc2.getX()) > Configuration.RANGE - 1) {
                    horizontalLine = false;
                    break outerLoop;
                }
            }
        }
        for (Location loc : move.getMove().keySet()) {
            for (Location loc2 : move.getMove().keySet()) {
                if (loc != loc2 && loc.isEqualTo(loc2)) {
                    throw new InvalidMoveException("Move contains the same tile twice or more.");
                }
            }
        }
        if (move.getMove().size() == 1) {
            move.setPositioning(Positioning.unspecified);
        } else if (horizontalLine && !verticalLine) {
            move.setPositioning(Positioning.horizontal);
        } else if (!horizontalLine && verticalLine) {
            move.setPositioning(Positioning.vertical);
        } else {
            move.setPositioning(Positioning.invalid);
            throw new InvalidMoveException("Invalid Positioning");
        }
        return true;
    }

    /**
     * Checks whether a move has all Tiles with the same color and different shape or same shape and different color.
     * @return True if the Tiles have valid shapes/colors
     */
    private boolean validIdentity() throws InvalidMoveException {
        boolean sameColor = true;
        boolean sameShape = true;

        for (Tile tile : move.getMove().values()) {
            for (Tile tile2 : move.getMove().values()) {
                if (tile != tile2 && tile.getColor() != tile2.getColor()) {
                    sameColor = false;
                }
                if (tile != tile2 && tile.getShape() != tile2.getShape()) {
                    sameShape = false;
                }
            }
        }

        if (move.getMove().size() == 1) {
            move.setIdentity(Identity.unspecified);
        } else if (sameColor && !sameShape) {
            move.setIdentity(Identity.color);
        } else if (!sameColor && sameShape) {
            move.setIdentity(Identity.shape);
        } else {
            move.setIdentity(Identity.invalid);
        }

        if (move.getIdentity() == Identity.color) {
            for (Tile tile : move.getMove().values()) {
                for (Tile tile2 : move.getMove().values()) {
                    if (tile != tile2 && tile.getShape() == tile2.getShape()) {
                        throw new InvalidMoveException("Move contains the same tile twice or more.");
                    }
                }
            }
        } else if (move.getIdentity() == Identity.shape) {
            for (Tile tile : move.getMove().values()) {
                for (Tile tile2 : move.getMove().values()) {
                    if (tile != tile2 && tile.getColor() == tile2.getColor()) {
                        throw new InvalidMoveException("Move contains the same tile twice or more.");
                    }
                }
            }
        } else if (move.getIdentity() == Identity.invalid){
            throw new InvalidMoveException("Identity is invalid.");
        }
        return true;
    }

    /**
     * Creates a List of Tiles from the field that form a line with the Tile that lies on startPoint. This method is recursive.
     * @param axis Axis on which the line lies.
     * @param location Location of Tile that is checked.
     * @param startPoint Location of the original Tile from the move that is checked.
     * @param step Step the X or Y takes (1 or -1).
     * @return A List of Tiles that form a line with the Tile that lies on startPoint.
     */
    private List<Tile> orthogonalLine(Axis axis, Location location, Location startPoint, int step) { //TODO: merge with createLine()
        List<Tile> line = new ArrayList<>();
        if (location.isEqualTo(startPoint)) {
            if (step > 0) {
                line.add(move.getMove().get(startPoint));
            }
            List<Tile> temp = axis == Axis.Y ? orthogonalLine(axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                    orthogonalLine(axis, new Location(location.getX(), location.getY() + step), startPoint, step);
            line.addAll(temp);
        } else {
            for (Location loc : board.getField().keySet()) {
                if (loc.isEqualTo(location)) {
                    line.add(board.getField().get(loc));
                    List<Tile> temp = axis == Axis.Y ? orthogonalLine(axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                            orthogonalLine(axis, new Location(location.getX(), location.getY() + step), startPoint, step);
                    line.addAll(temp);
                    break; //right location is found, further looping is unnecessary
                }
            }
        }
        return line;
    }

    /**
     * Checks whether a line is valid according to the game rules.
     * @param line Line to be checked.
     * @return True if the line is valid.
     */
    private boolean validLine(List<Tile> line) throws InvalidMoveException {
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
            for (Tile tile : line) {
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
     * @param axis Axis on which the line lies.
     * @param location Location of Tile that is checked.
     * @param step Step the X or Y takes (1 or -1).
     * @return True if the move does not violate any game rules on the line.
     */
    private boolean validLine(Axis axis, Location location, int step) throws InvalidMoveException { // step is either +1 or -1
        for (Location loc : move.getMove().keySet()) {
            if (loc.isEqualTo(location)) {
                if (axis == Axis.X) {
                    return validLine(axis, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                } else {
                    return validLine(axis, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                }
            }
        }

        for (Location loc : board.getField().keySet()) {
            if (loc.isEqualTo(location)) {

                //same color, different shapes
                if (move.getIdentity() == Identity.color) {
                    for (Tile tile : move.getMove().values()) {
                        if (board.getField().get(loc).getColor() != tile.getColor()) {
                            throw new InvalidMoveException("Color doesn't fit in row/column.");
                        }
                        if (board.getField().get(loc).getShape() == tile.getShape()) {
                            throw new InvalidMoveException("Shape is already in row/column.");
                        }
                    }
                    if (axis == Axis.X) {
                        return validLine(axis, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                    } else {
                        return validLine(axis, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                    }

                    //same shape, different colors
                } else if (move.getIdentity() == Identity.shape) {
                    for (Tile tile : move.getMove().values()) {
                        if (board.getField().get(loc).getShape() != tile.getShape()) {
                            throw new InvalidMoveException("Shape doesn't fit in row/column.");
                        }
                        if (board.getField().get(loc).getColor() == tile.getColor()) {
                            throw new InvalidMoveException("Color is already in row/column.");
                        }
                    }
                    if (axis == Axis.X) {
                        return validLine(axis, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                    } else {
                        return validLine(axis, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                    }

                    //one block in move set
                } else if (move.getIdentity() == Identity.unspecified) {
                    for (Tile tile : move.getMove().values()) {
                        if (board.getField().get(loc).getColor() == tile.getColor() &&
                                board.getField().get(loc).getShape() == tile.getShape()) {
                            throw new InvalidMoveException("Same tile in row/column.");
                        }
                        if (board.getField().get(loc).getColor() != tile.getColor() &&
                                board.getField().get(loc).getShape() != tile.getShape()) {
                            throw new InvalidMoveException("Tile shares no identity with surrounding tiles.");
                        }
                    }
                } else {
                    //should not occur
                    throw new InvalidIdentityRuntimeException();
                }

                break; // right location is found on the field, no need to continue loop
            }
        }
        if (axis == Axis.X) {
            if (location.getX() < board.higherBound(axis, move.getMove()).getX() && location.getX() > board.lowerBound(axis, move.getMove()).getX()) {
                return validLine(axis, new Location(location.getX() + step, location.getY()), step);                // recursive
            }
        } else {
            if (location.getY() < board.higherBound(axis, move.getMove()).getY() && location.getY() > board.lowerBound(axis, move.getMove()).getY()) {
                return validLine(axis, new Location(location.getX(), location.getY() + step), step);                // recursive
            }
        }

        return true;
    }


}
