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
 *
 * This class has several methods that are used to validate whether a PutMove is valid on a provided Board.
 */
public class PutMoveValidator {

    /**
     * The Board where the PutMove is tested for validity.
     */
    private Board board;

    /**
     * The PutMove that is tested for validity.
     */
    private PutMove move;

    /**
     * Boolean that holds information on whether this PutMove is the first PutMove on the Board.
     */
    private boolean firstMove;

    /**
     * Creates a new instance of PutMoveValidator. This class is used to validate a PutMove.
     * @param board The board on which the move must be validated.
     * @param move The move to be validated.
     */
    public PutMoveValidator(Board board, PutMove move) {
        this.board = board;
        this.move = move;
        firstMove = board.getField().isEmpty();
    }

    /**
     * This method is called by other classes to check this move's validity on the board. If the move is invalid in
     * any way, an InvalidMoveException is thrown.
     * @return True if the move is valid on the board.
     * @throws InvalidMoveException When the move is invalid in any way.
     */
    public boolean validMove() throws InvalidMoveException {
        boolean valid;

        // if this move is the first move on the board
        if (firstMove) {
            boolean validFirstMove = false;

            // check if the move contains a Location pointing to (0, 0)
            for (Location loc : move.getMove().keySet()) {
                if (loc.equals(0, 0)) {
                    validFirstMove = true;
                    break;
                }
            }
            // if there is no Location in the first move pointing to (0, 0), throw invalid move exception
            if (!validFirstMove) throw new InvalidMoveException("First move does not have a Tile on Location (0, 0).");
        }
        // check whether the move is invalid regardless of the field's state, if not, throw an invalid move exception
        if (validPositioning() && validIdentity()) valid = true;
        else throw new InvalidMoveException("Move is invalid regardless of the fields's state.");

        // check whether the move is valid on the board
        if (!validPut()) valid = false;

        return valid;
    }

    /**
     * Checks whether a PutMove is valid. A PutMove is invalid if all Tiles in a line have the same color and a
     * different shape or all the same shape and different shape. The same Tile in a line is not allowed and the move
     * must be connected to the field, but may not override it.
     * @return True if the move is valid on this board.
     * @throws InvalidMoveException If the move is invalid.
     */
    private boolean validPut() throws InvalidMoveException {
        boolean validLine;
        boolean validOrthogonalLines = true;
        boolean touchesField = false;

        // if this is not the first move on the board
        if (!firstMove) {

            // check for every location in the move and on the board
            for (Location loc : move.getMove().keySet()) {
                for (Location loc2 : board.getField().keySet()) {

                    // check if location is not already in use
                    if (loc.equals(loc2)) throw new InvalidMoveException("Location already in use.");

                    // check whether at least one tile in the move makes contact with at least one tile on the field
                    if (!touchesField) {
                        if (loc.equals(loc2.getX(), loc2.getY() + 1) || loc.equals(loc2.getX(), loc2.getY() - 1) || loc.equals(loc2.getX() + 1, loc2.getY()) ||
                                loc.equals(loc2.getX() - 1, loc2.getY())) {
                            touchesField = true;
                        }
                    }
                }
            }
            // if no tiles in the move touch the tiles in the field, the move is invalid.
            if (!touchesField) {
                throw new InvalidMoveException("Tiles are not connected to any other Tiles in the field.");
            }
        }
        // pick a random Location as starting point from the move
        Location startPoint = new ArrayList<>(move.getMove().keySet()).get(0);

        // if the move line is vertical
        if (move.getPositioning() == Positioning.vertical) {

            // Check whether the line itself is valid
            validLine = validLine(Axis.Y, startPoint, 1) && validLine(Axis.Y, startPoint, -1);

            // Check whether the orthogonal lines are valid
            for (Location loc : move.getMove().keySet()) {

                // initialize orthogonal line
                List<Tile> orthogonalLine = new ArrayList<>();
                orthogonalLine.addAll(orthogonalLine(Axis.Y, loc, loc, 1));
                orthogonalLine.addAll(orthogonalLine(Axis.Y, loc, loc, -1));

                // validate the line
                if (orthogonalLine.size() > 1 && !validIdentityInLine(orthogonalLine)) {
                    validOrthogonalLines = false;
                    break;
                }
            }

        // if the move line is horizontal
        } else if (move.getPositioning() == Positioning.horizontal) {

            // check whether the line itself is valid
            validLine = validLine(Axis.X, startPoint, 1) && validLine(Axis.X, startPoint, -1);

            // check whether the orthogonal lines are valid
            for (Location loc : move.getMove().keySet()) {

                // initialize orthogonal line
                List<Tile> orthogonalLine = new ArrayList<>();
                orthogonalLine.addAll(orthogonalLine(Axis.X, loc, loc, 1));
                orthogonalLine.addAll(orthogonalLine(Axis.X, loc, loc, -1));

                // validate the line
                if (orthogonalLine.size() > 1 && !validIdentityInLine(orthogonalLine)) {
                    validOrthogonalLines = false;
                    break;
                }
            }

        // if the move consists of only one block
        } else if (move.getPositioning() == Positioning.unspecified) {

            // check whether the line is valid on the X axis
            validLine = validLine(Axis.X, startPoint, 1) && validLine(Axis.X, startPoint, -1);

            // check whether the line is valid on the Y axis
            validOrthogonalLines = validLine(Axis.Y, startPoint, 1) &&
                    validLine(Axis.Y, startPoint, -1);

        // if the positioning of the move is invalid
        } else {
            //should not occur
            throw new InvalidPositioningRuntimeException();
        }
        return validLine && validOrthogonalLines;
    }

    /**
     * Checks whether a move is either all on one horizontal line or all on one vertical line and within
     * Configuration.RANGE distance from one another. Also checks whether the move set has duplicates.
     * @return True if the Location of the move is valid
     */
    private boolean validPositioning() throws InvalidMoveException {
        boolean verticalLine = true;
        boolean horizontalLine = true;

        // checks whether the tiles are all on one horizontal line and whether they are close enough to each other
        outerLoop:
        for (Location loc : move.getMove().keySet()) {
            for (Location loc2 : move.getMove().keySet()) {
                if (loc.getX() != loc2.getX() || Math.abs(loc.getY() - loc2.getY()) > Configuration.RANGE - 1) {
                    verticalLine = false;
                    break outerLoop;
                }
            }
        }

        // check whether the tiles are all on the one vertical line and whether they are close enough to each other
        outerLoop:
        for (Location loc : move.getMove().keySet()) {
            for (Location loc2 : move.getMove().keySet()) {
                if (loc.getY() != loc2.getY() || Math.abs(loc.getX() - loc2.getX()) > Configuration.RANGE - 1) {
                    horizontalLine = false;
                    break outerLoop;
                }
            }
        }

        // checks whether the move contains a single location twice
        for (Location loc : move.getMove().keySet()) {
            for (Location loc2 : move.getMove().keySet()) {
                if (loc != loc2 && loc.equals(loc2)) {
                    throw new InvalidMoveException("Move contains the same tile twice or more.");
                }
            }
        }

        // sets the positioning of the move to the appropriate value
        if (move.getMove().size() == 1) {
            move.setPositioning(Positioning.unspecified);
        } else if (horizontalLine && !verticalLine) {
            move.setPositioning(Positioning.horizontal);
        } else if (!horizontalLine && verticalLine) {
            move.setPositioning(Positioning.vertical);
        } else {
            // the move is invalid
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

        // compare all tiles in the move
        for (Tile tile : move.getMove().values()) {
            for (Tile tile2 : move.getMove().values()) {

                // check if the tiles have the same color
                if (tile != tile2 && tile.getColor() != tile2.getColor()) sameColor = false;

                // check if the tiles have the same shape
                if (tile != tile2 && tile.getShape() != tile2.getShape()) sameShape = false;
            }
        }

        // sets the identity of the move to the appropriate value
        if (move.getMove().size() == 1) {
            move.setIdentity(Identity.unspecified);
        } else if (sameColor && !sameShape) {
            move.setIdentity(Identity.color);
        } else if (!sameColor && sameShape) {
            move.setIdentity(Identity.shape);
        } else {
            move.setIdentity(Identity.invalid);
        }

        // check if there's a tile that appears twice in the move for a color- and shape identity.
        // Note: the unspecified identity does not have to be checked, since there's only one Tile in such a move.
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

        // throw an exception if the identity is invalid
        } else if (move.getIdentity() == Identity.invalid){
            throw new InvalidMoveException("Identity is invalid.");
        }

        return true;
    }

    /**
     * Creates a List of Tiles from the field that form a line with the Tile that lies on startPoint.
     * This method is recursive.
     * @param axis Axis on which the line lies.
     * @param location Location of Tile that is checked.
     * @param startPoint Location of the original Tile from the move that is checked.
     * @param step Step the X or Y takes (1 or -1).
     * @return A List of Tiles that form a line with the Tile that lies on startPoint.
     */
    private List<Tile> orthogonalLine(Axis axis, Location location, Location startPoint, int step) {
        List<Tile> line = new ArrayList<>();

        // first iteration of this recursive method
        if (location.equals(startPoint)) {

            // if the method is going over the positive side of the axis, add the tile on the starting point to the list
            if (step > 0) line.add(move.getMove().get(startPoint));

            // create a new list of Tiles with a new location, a Location with the appropriate axis increased by step (X + step, Y) or (X, Y + step)
            List<Tile> temp = axis == Axis.Y ? orthogonalLine(axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                    orthogonalLine(axis, new Location(location.getX(), location.getY() + step), startPoint, step);

            // add the new list to the existing list
            line.addAll(temp);

        // if the location is not equal to the starting point
        } else {

            // check every filled location on the board
            for (Location loc : board.getField().keySet()) {

                // if this method's parameter 'location' is on the board:
                if (loc.equals(location)) {

                    // add the tile on this location to the list
                    line.add(board.getField().get(loc));

                    // create a new list of Tiles with a new location, a Location with the appropriate axis increased by step (X + step, Y) or (X, Y + step)
                    List<Tile> temp = axis == Axis.Y ? orthogonalLine(axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                            orthogonalLine(axis, new Location(location.getX(), location.getY() + step), startPoint, step);

                    // add the new list to the existing list
                    line.addAll(temp);

                    //right location is found, further looping is unnecessary
                    break;
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
    private boolean validIdentityInLine(List<Tile> line) throws InvalidMoveException {
        boolean valid = true;
        boolean sameColor = true;
        boolean sameShape = true;

        // compare all Tiles in 'line'
        for (Tile tile : line) {
            for (Tile tile2 : line) {

                // check whether the tiles have the same color or the same shape
                if (tile != tile2 && tile.getColor() != tile2.getColor()) sameColor = false;
                else if (tile != tile2 && tile.getShape() != tile2.getShape()) sameShape = false;
            }
        }

        // check if there's a tile that appears twice in the line for a color- and shape identity.
        // note: there's no need for checking for a single tile, since that is checked before calling this method.
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

        // if the tiles all have the same color or there are tiles with both different colors and different shapes
        } else valid = false;

        return valid;
    }

    /**
     * Checks whether a move with a certain axis is valid on the field. This method only checks the horizontal or
     * vertical line, depending on the axis given. It's a recursive method that uses a given location and increases
     * either the X or Y variable (again, depending on the given axis) by the given step. The step variable
     * is either 1 or -1.
     * @param axis Axis on which the line lies.
     * @param location Location of Tile that is checked.
     * @param step Step the X or Y takes (1 or -1).
     * @return True if the move does not violate any game rules on the line.
     */
    private boolean validLine(Axis axis, Location location, int step) throws InvalidMoveException {

        // If the parameter 'location' is in the move, increase the appropriate axis by 'step' and try again recursively
        for (Location loc : move.getMove().keySet()) {
            if (loc.equals(location)) {
                if (axis == Axis.X) {
                    return validLine(axis, new Location(loc.getX() + step, loc.getY()), step);          // recursive
                } else {
                    return validLine(axis, new Location(loc.getX(), loc.getY() + step), step);          // recursive
                }
            }
        }

        // Check whether the parameter 'location' is on the board
        for (Location loc : board.getField().keySet()) {
            if (loc.equals(location)) {

                // Check whether the Tile on 'location' fits in the line of the move in case of:
                // - The same color and different shapes:
                if (move.getIdentity() == Identity.color) {
                    for (Tile tile : move.getMove().values()) {

                        // Tile on 'location' has different color
                        if (board.getField().get(loc).getColor() != tile.getColor()) {
                            throw new InvalidMoveException("Color doesn't fit in row/column.");
                        }

                        // Tile on 'location' has the same shape as one of the Tiles in the move
                        if (board.getField().get(loc).getShape() == tile.getShape()) {
                            throw new InvalidMoveException("Shape is already in row/column.");
                        }
                    }

                    // try next location by increasing appropriate axis by 'step'
                    return axis == Axis.X ? validLine(axis, new Location(loc.getX() + step, loc.getY()), step) :
                            validLine(axis, new Location(loc.getX(), loc.getY() + step), step);

                // - The same shape and different colors:
                } else if (move.getIdentity() == Identity.shape) {
                    for (Tile tile : move.getMove().values()) {

                        // Tile on 'location' has different shape
                        if (board.getField().get(loc).getShape() != tile.getShape()) {
                            throw new InvalidMoveException("Shape doesn't fit in row/column.");
                        }

                        // Tile on 'location' has the same color as one of the Tiles in the move
                        if (board.getField().get(loc).getColor() == tile.getColor()) {
                            throw new InvalidMoveException("Color is already in row/column.");
                        }
                    }

                    // try next location by increasing appropriate axis by 'step'
                    return axis == Axis.X ? validLine(axis, new Location(loc.getX() + step, loc.getY()), step) :
                            validLine(axis, new Location(loc.getX(), loc.getY() + step), step);

                // if there's only one block in move
                } else if (move.getIdentity() == Identity.unspecified) {
                    for (Tile tile : move.getMove().values()) {

                        // Check if there's an equivalent tile in the line already
                        if (board.getField().get(loc).getColor() == tile.getColor() &&
                                board.getField().get(loc).getShape() == tile.getShape()) {
                            throw new InvalidMoveException("Same tile in row/column.");
                        }

                        // Check if tile fits with other tiles in the line
                        if (board.getField().get(loc).getColor() != tile.getColor() &&
                                board.getField().get(loc).getShape() != tile.getShape()) {
                            throw new InvalidMoveException("Tile shares no identity with surrounding tiles.");
                        }
                    }

                // if the identity is invalid, this should not occur
                } else throw new InvalidIdentityRuntimeException();

                // right location is found on the field, no need to continue loop
                break;
            }
        }
        // this part is reached when there is no Tile on the parameter 'location'
        // or when all previous recursive iterations have ended

        // if the axis is the X-axis
        if (axis == Axis.X) {

            // if the X of parameter 'location' is between the higher and lower bound of the move,
            // execute another iteration of this method with the location's X increased by step.
            if (location.getX() < board.higherBound(axis, move.getMove()).getX() &&
                    location.getX() > board.lowerBound(axis, move.getMove()).getX()) {
                return validLine(axis, new Location(location.getX() + step, location.getY()), step);
            }

        // if the axis is the Y-axis
        } else {

            // if the Y of parameter 'location' is between the higher and lower bound of the move,
            // execute another iteration of this method with the location's Y increased by step.
            if (location.getY() < board.higherBound(axis, move.getMove()).getY() &&
                    location.getY() > board.lowerBound(axis, move.getMove()).getY()) {
                return validLine(axis, new Location(location.getX(), location.getY() + step), step);
            }
        }

        return true;
    }


}
