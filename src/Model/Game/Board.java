package Model.Game;

import Model.Game.Enumerations.Axis;
import Model.Game.Enumerations.Identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martijn on 11-1-2016.
 *
 * This class represents the board that the game is played on. The actual field with tiles is a Map with Locations and Tiles.
 * There are methods to alter that field, like making a move, and to get information from it, like getting possible moves.
 */
public class Board {

    /**
     * The actual field with all the tiles and their location.
     */
    private Map<Location, Tile> field;

    /**
     * Creates a new instance of Board with a field. The field is a Map with a Location and a Tile.
     */
    public Board() {
        field = new HashMap<>();
    }

    /**
     * Returns the field.
     * @return This board's field.
     */
    public Map<Location, Tile> getField() {
        return field;
    }

    /**
     * Makes a put move on the board. This method does NOT check the validity of the move.
     * @param move The move to be made.
     */
    public void makePutMove(PutMove move) {
        field.putAll(move.getMove());
    }

    /**
     * Creates a Map with a Location and a List of Tiles. The Locations are open spaces in the field where a Tile could be placed.
     * For every Location in the Map a List of Tiles is created that contains all Tiles that could be placed there.
     * If a Location yields an empty List, the Location is not included in the Map.
     * @return A Map with a Location and a List of Tiles that can be placed on that Location.
     */
    public Map<Location, List<Tile>> getPossibleMoves() {
        Map<Location, List<Tile>> possibleMoves = new HashMap<>();

        // Create a list of all the valid locations where a tile could be placed in the field.
        List<Location> openLocations = getOpenLocations();

        // Check for every location in the openLocations
        for (Location loc : openLocations) {

            // Make a list of all Tiles in a line horizontally and vertically
            List<Tile> tilesX = createLine(Axis.X, loc, loc, 1);
            List<Tile> tilesY = createLine(Axis.Y, loc, loc, 1);

            // Get the identity of those lines
            Identity identityX = getIdentity(tilesX);
            Identity identityY = getIdentity(tilesY);

            // if there exists a Tile that could be placed on the location without invalidating the lines (rough estimate))
            if (tilesX.size() < 6 && identityX != Identity.invalid && tilesY.size() < 6 && identityY != Identity.invalid) {

                // initialize list variables
                List<Tile> possibleTiles = new ArrayList<>();
                List<Tile> listX = new ArrayList<>();
                List<Tile> listY = new ArrayList<>();

                // if the list of Tiles on the X-axis is not empty
                if (!tilesX.isEmpty()) {

                    // if the Tiles on the X-axis are all of the same color, add all unused Tiles of that color to listX
                    if (identityX == Identity.color) {
                        listX = checkId(listX, tilesX, identityX, tilesX.get(0).getColor().getId(), 1);

                    // if the Tiles on the X-axis are all of the same shape, add all unused Tiles of that shape to listX
                    } else if (identityX == Identity.shape) {
                        listX = checkId(listX, tilesX, identityX, tilesX.get(0).getShape().getId(), Configuration.RANGE);

                    // if the the X-axis consists of only one Tile, add all unused Tiles of the right shape and color combinations to listX
                    } else if (identityX == Identity.unspecified) {
                        listX = checkId(listX, tilesX, Identity.color, tilesX.get(0).getColor().getId(), 1);
                        listX = checkId(listX, tilesX, Identity.shape, tilesX.get(0).getShape().getId(), Configuration.RANGE);
                    }
                }

                // if the list of Tiles on the Y-axis is not empty
                if (!tilesY.isEmpty()) {

                    // if the Tiles on the Y-axis are all of the same color, add all unused Tiles of that color to listY
                    if (identityY == Identity.color) {
                        listY = checkId(listY, tilesY, identityY, tilesY.get(0).getColor().getId(), 1);

                    // if the Tiles on the Y-axis are all of the same shape, add all unused Tiles of that shape to listY
                    } else if (identityY == Identity.shape) {
                        listY = checkId(listY, tilesY, identityY, tilesY.get(0).getShape().getId(), Configuration.RANGE);

                    // if the the Y-axis consists of only one Tile, add all unused Tiles of the right shape and color combinations to listY
                    } else if (identityY == Identity.unspecified) {
                        listY = checkId(listY, tilesY, Identity.color, tilesY.get(0).getColor().getId(), 1);
                        listY = checkId(listY, tilesY, Identity.shape, tilesY.get(0).getShape().getId(), Configuration.RANGE);
                    }
                }

                // if there are no possible Tiles on the X-axis but there are possible Tiles on the Y-axis,
                // add all those Tiles to possibleTiles (unless they're already in possibleTiles)
                if (listX.isEmpty() && !listY.isEmpty()) {
                    for (Tile tile : listY) {
                        if (!possibleTiles.contains(tile)) possibleTiles.add(tile);
                    }

                // if there are no possible Tiles on the Y-axis but there are possible Tiles on the X-axis,
                // add all those Tiles to possibleTiles (unless they're already in possibleTiles)
                } else if (!listX.isEmpty() && listY.isEmpty()) {
                    for (Tile tile : listX) {
                        if (!possibleTiles.contains(tile)) possibleTiles.add(tile);
                    }

                } else {

                    // Add every Tile that's both possible on the X- as well as the Y-axis to possibleTiles
                    // (unless they're already in possibleTiles)
                    for (Tile tile : listX) {
                        boolean add = false;
                        for (Tile tile2 : listY) {
                            if (tile.equals(tile2)) {
                                add = true;
                                break;
                            }
                        }
                        if (add && !possibleTiles.contains(tile)) possibleTiles.add(tile);
                    }
                }

                // if a location has possible moves, add them to the possibleMoves map
                if (!possibleTiles.isEmpty()) possibleMoves.put(loc, possibleTiles);
            }
        }
        return possibleMoves;
    }

    /**
     * Checks all ID's of Tiles with a certain identity if they are in <code>tiles</code>. If not,
     * they will be added to <code>list</code>.
     * @param list List of possible Tiles for a certain Location on the board.
     * @param tiles List of Tiles that are in a line on the board.
     * @param id ID of the identity of the Tile concerned.
     * @param step Step taken by for loop that is determined by the identity.
     * @return A list with usable Tiles.
     */
    public List<Tile> checkId(List<Tile> list, List<Tile> tiles, Identity identity, int id, int step) {

        // initialize maximum range of for-loop
        int max = 0;

        if (identity == Identity.color) {
            // set id to color id to match Tile
            id = id * Configuration.RANGE;

            // set maximum range of for-loop
            max = id + Configuration.RANGE;

        } else if (identity == Identity.shape) {
            // set maximum range of for-loop
            max = Configuration.RANGE * Configuration.RANGE;
        }

        // loop through all tiles with an id matching either the color or shape of initial Tile
        for (int i = id; i < max; i += step) {
            // initialize loop values
            Tile temp = new Tile(i);
            boolean add = true;

            // check if tile is in List<Tile> tiles, if so, don't add it to List<Tile> list
            for (Tile tile : tiles) {
                if (tile.equals(temp)) {
                    add = false;
                    break;
                }
            }

            // if tempTile is not in List<Tiles> tiles, add it to List<Tile> list
            if (add) list.add(temp);
        }
        return list;
    }

    /**
     * Checks all Tiles in a List and determines its identity.
     * @param line List of Tiles to be checked.
     * @return Identity of List of Tiles.
     */
    public Identity getIdentity(List<Tile> line) {
        // initialize values
        Identity identity;
        boolean sameColor = true;
        boolean sameShape = true;

        // if the line is empty, return invalid. If the line is only 1 tile long, return unspecified.
        if (line.size() < 2) return line.isEmpty() ? Identity.invalid : Identity.unspecified;

        // check if all tiles have the same color or the same shape
        for (Tile tile : line) {
            for (Tile tile2 : line) {
                if (tile != tile2 && tile.getColor() != tile2.getColor()) sameColor = false;
                if (tile != tile2 && tile.getShape() != tile2.getShape()) sameShape = false;
            }
        }

        // if all tiles have the same color and not the same shape, then the identity is color
        if (sameColor && !sameShape) {
            identity = Identity.color;
            outerLoop:
            for (Tile tile : line) {
                for (Tile tile2 : line) {
                    if (tile != tile2 && tile.getShape() == tile2.getShape()) {
                        // tiles are the same (same color and shape), so identity is invalid
                        identity = Identity.invalid;
                        break outerLoop;
                    }
                }
            }

        // if all tiles have the same shape and not the same color, then the identity is shape
        } else if (!sameColor && sameShape) {
            identity = Identity.shape;
            outerLoop:
            for (Tile tile : line) {
                for (Tile tile2 : line) {
                    if (tile != tile2 && tile.getColor() == tile2.getColor()) {
                        // tiles are the same (same color and shape), so identity is invalid
                        identity = Identity.invalid;
                        break outerLoop;
                    }
                }
            }

        // all other combinations just mean the identity is invalid
        } else {
            identity = Identity.invalid;
        }
        return identity;
    }

    /**
     * Creates a List of Tiles that are on one line on a certain axis. The Location startPoint is not included.
     * This method uses recursion to create the List. On the first call to this method, the step should be positive (1).
     * @param axis The axis on which the line lies.
     * @param location The location checked in this iteration of the method.
     * @param startPoint The start location of the method. This location is the reference point.
     * @param step The step taken on the line to the next location. Must be either 1 or -1.
     * @return A list of Tiles that lie on one line. The Location startPoint is not included.
     */
    //@ requires step == 1 or step == -1;
    public List<Tile> createLine(Axis axis, Location location, Location startPoint, int step) {

        // initialize a list of Tiles as the line
        List<Tile> line = new ArrayList<>();

        // if the location is the starting point
        if (location.equals(startPoint)) {

            // create a new list of Tiles with this method (recursively) with either the X- or the Y-axis incremented by step
            List<Tile> temp = axis == Axis.X ? createLine(axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                    createLine(axis, new Location(location.getX(), location.getY() + step), startPoint, step);

            // add the resulting list to line
            line.addAll(temp);

        // if the location is not the starting point
        } else {

            // for every tile on the field, check if the location is that tile's location
            for (Location loc : field.keySet()) {
                if (loc.equals(location)) {

                    // add the location to the field
                    line.add(field.get(loc));

                    // create a new list of Tiles with this method (recursively) with either the X- or the Y-axis incremented by step
                    List<Tile> temp = axis == Axis.X ? createLine(axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                            createLine(axis, new Location(location.getX(), location.getY() + step), startPoint, step);

                    // add the resulting list to the line
                    line.addAll(temp);

                    //right location is found, further looping is unnecessary
                    break;
                }
            }

            // if the step is positive
            if (step > 0) {

                // create a new list of Tiles with this method (recursively) with either the X- or the Y-axis decremented by step
                List<Tile> temp = axis == Axis.X ? createLine(axis, new Location(startPoint.getX() - step, startPoint.getY()), startPoint, -step) :
                        createLine(axis, new Location(startPoint.getX(), startPoint.getY() - step), startPoint, -step);

                // add the resulting list to the line
                line.addAll(temp);
            }
        }

        // if the step is negative and no further locations are found, return the line
        return line;
    }

    /**
     * Creates a List of all empty Locations adjacent to used Locations. This List is used to determine possible moves.
     * @return A List of all empty Locations adjacent to used Locations.
     */
    public List<Location> getOpenLocations() {

        // initialize list of open Locations
        List<Location> openLocations = new ArrayList<>();

        // check if field has tiles
        if (!field.isEmpty()) {

            // for every tile on the field
            for (Location loc : field.keySet()) {

                // initialize a list of locations with all adjacent locations
                List<Location> temp = new ArrayList<>();
                temp.add(new Location(loc.getX() + 1, loc.getY()));
                temp.add(new Location(loc.getX() - 1, loc.getY()));
                temp.add(new Location(loc.getX(), loc.getY() + 1));
                temp.add(new Location(loc.getX(), loc.getY() - 1));

                // for every location adjacent to the tile
                for (Location loc2 : temp) {

                    // initialize boolean that determines whether to add the loc2 to openLocations
                    boolean add = true;

                    // if the location is already in use by another tile, don't add it to openLocations
                    for (Location loc3 : field.keySet()) {
                        if (loc2.equals(loc3)) {
                            add = false;
                            break;
                        }
                    }

                    // if the location is not in openLocations yet and it add is true, add the location to openLocations
                    if (!openLocations.contains(loc2) && add) openLocations.add(loc2);
                }
            }

        // if field is empty, give (0, 0) as open location
        } else openLocations.add(new Location(0, 0));

        return openLocations;
    }

    /**
     * Gives the Location with the lowest X or Y depending on the parameter, axis, from a Location to Tile Map.
     * This is done using bubblesort, since N <= 108, making bubblesort sufficiently efficient.
     * @param axis Determines whether the Location with the lowest X or Y is returned.
     * @param map The Map where the Location is returned from.
     * @return Location with lowest X/Y
     */
    public Location lowerBound(Axis axis, Map<Location, Tile> map) {

        // Put all locations from the map into a list.
        List<Location> list = new ArrayList<>();
        list.addAll(map.keySet());

        // if the list is empty or consists of only one Location, return null or that Location respectively
        if (list.size() < 2) {
            return list.isEmpty() ? null : list.get(0);
        }

        // apply bubblesort...
        for (int i = 0; i < list.size() - 1; i++) {

            // ... on the X-axis.
            if (axis == Axis.X) {
                if (list.get(i).getX() < list.get(i + 1).getX()) {
                    Location temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                }

            // ... on the Y-axis.
            } else if (axis == Axis.Y) {
                if (list.get(i).getY() < list.get(i + 1).getY()) {
                    Location temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                }
            }
        }
        // return the last in the list (lower bound)
        return list.get(list.size() - 1);
    }

    /**
     * Gives the Location with the highest X or Y depending on the parameter, axis, from a Location to Tile Map.
     * This is done using bubblesort, since N <= 108, making bubblesort sufficiently efficient.
     * @param axis Determines whether the Location with the highest X or Y is returned.
     * @param map The Map where the Location is returned from.
     * @return Location with highest X/Y
     */
    public Location higherBound(Axis axis, Map<Location, Tile> map) {

        // Put all locations from the map into a list.
        List<Location> list = new ArrayList<>();
        list.addAll(map.keySet());

        // if the list is empty or consists of only one Location, return null or that Location respectively
        if (list.size() < 2) {
            return list.isEmpty() ? null : list.get(0);
        }

        // apply bubblesort...
        for (int i = 0; i < list.size() - 1; i++) {

            // ... on the X-axis.
            if (axis == Axis.X) {
                if (list.get(i).getX() > list.get(i + 1).getX()) {
                    Location temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                }

            // ... on the Y-axis.
            } else if (axis == Axis.Y) {
                if (list.get(i).getY() > list.get(i + 1).getY()) {
                    Location temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                }
            }
        }
        // return the last in the list (upper bound)
        return list.get(list.size() - 1);
    }

    /**
     * Creates a string representation of the field that can be used in a TUI. This format will work as long as the
     * coordinates do not enter triple digits. Considering there are only 108 tiles, this should not be a problem.
     * @return String representation of the field.
     */
    public String toString() {

        // If the field is empty return a predetermined String.
        if (field.isEmpty()) return Configuration.EMPTY_FIELD;

        // define the lower and upper bounds of the X- and Y-axis.
        int lowX = lowerBound(Axis.X, field).getX();
        int highX = higherBound(Axis.X, field).getX();
        int lowY = lowerBound(Axis.Y, field).getY();
        int highY = higherBound(Axis.Y, field).getY();

        // define the length of the rows
        int length = highX - lowX + 2;

        // initialize static- and empty row
        String emptyRow = "";
        String staticRow = "---|";

        // fill the static- and empty row
        for (int i = 0; i < length; i++) {
            staticRow += Configuration.MID_ROW;
            emptyRow += Configuration.EMPTY_SPACE;
        }

        // finish static- and empty row (start of empty row is added later, it's not the same)
        staticRow += Configuration.END_ROW + "\n";
        emptyRow += Configuration.EMPTY_SPACE + "\n";

        // initialize the fieldString
        String fieldString = staticRow + startRow(highY + 1) + emptyRow + staticRow;

        // build the string starting from the top row
        for (int j = highY; j > lowY - 1; j--) {

            // initialize row
            String row = startRow(j) + Configuration.EMPTY_SPACE;

            // create a map with all Location - Tile combinations that are on this row.
            Map<Location, Tile> temp = new HashMap<>();
            for (Location loc : field.keySet()) {
                if (loc.getY() == j) temp.put(loc, field.get(loc));
            }

            // fill in the row with the correct Tile
            for (int i = lowX; i <= highX; i++) {
                boolean placed = false;
                for (Location loc : temp.keySet()) {
                    if (loc.getX() == i) {
                        row += temp.get(loc) + "|";
                        placed = true;
                    }
                }
                // if the location doesn't have a Tile, add empty space.
                if (!placed) row += Configuration.EMPTY_SPACE;
            }

            // finalize row
            row += Configuration.EMPTY_SPACE + "\n";

            // add row to fieldString and a staticRow underneath
            fieldString += row + staticRow;
        }

        // add empty bottom row.
        fieldString += startRow(lowY - 1) + emptyRow + staticRow;

        // finalize fieldString with a proper String presentation of the X-axis
        fieldString += "y/x|";
        for (int i = lowX - 1; i <= highX + 1; i++) {
            fieldString += bottomRow(i);
        }

        // add new line and return the result
        fieldString += "\n";
        return fieldString;
    }

    /**
     * Creates a String that is used at the start of a row that represents the Y axis.
     * @param i Coordinate on the Y axis.
     * @return The coordinate on the Y axis in a proper format.
     */
    private String startRow(int i) {

        // add a minus if the coordinate is negative.
        String startRow = i < 0 ? "-" : " ";

        // add a 0 if the coordinate is single digit.
        if (i >= - 9 && i <= 9) {
            startRow += "0";
        }

        // add absolute value of coordinate and finalize String
        startRow += Math.abs(i) + "|";
        return startRow;
    }

    /**
     * Creates a String that is used as the bottom row that represents the X axis.
     * @param i Coordinate on the X axis.
     * @return The bottom row of the toString() method with the coordinates of the X axis in the proper format.
     */
    private String bottomRow(int i) {

        // add a minus if the coordinate is negative.
        String topRow = i < 0 ? "  -" : "   ";

        // add a 0 if the coordinate is single digit.
        if (i >= - 9 && i <= 9) {
            topRow += "0";
        }

        // add absolute value of coordinate and finalize String
        topRow += Math.abs(i) + "   |";
        return topRow;
    }

}
