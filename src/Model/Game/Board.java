package Model.Game;

import Model.Game.Enumerations.Axis;
import Model.Game.Enumerations.Identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martijn on 11-1-2016.
 */
public class Board {

    private Map<Location, Tile> field;

    /**
     * Creates a new instance of Board with a field. The field is a Map with a Location and a Tile.
     */
    public Board() {
        field = new HashMap<>();
    }

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
     * For every Location in the Map a List of Tiles is created that contains all Tiles that could be placed there. If a Location
     * yields an empty List, the Location is not included in the Map.
     * @return A Map with a Location and a List of Tiles that can be placed on that Location.
     */
    public Map<Location, List<Tile>> getPossibleMoves() {
        Map<Location, List<Tile>> possibleMoves = new HashMap<>();
        List<Location> openLocations = getOpenLocations();
        for (Location loc : openLocations) {

            List<Tile> tilesX = createLine(Axis.X, loc, loc, 1);
            Identity identityX = getIdentity(tilesX);

            List<Tile> tilesY = createLine(Axis.Y, loc, loc, 1);
            Identity identityY = getIdentity(tilesY);

            if (tilesX.size() < 6 && identityX != Identity.invalid && tilesY.size() < 6 && identityY != Identity.invalid) {
                List<Tile> possibleTiles = new ArrayList<>();
                List<Tile> listX = new ArrayList<>();
                List<Tile> listY = new ArrayList<>();

                if (!tilesX.isEmpty()) {
                    if (identityX == Identity.color) {
                        listX = checkId(listX, tilesX, identityX, tilesX.get(0).getColor().getId(), 1);
                    } else if (identityX == Identity.shape) {
                        listX = checkId(listX, tilesX, identityX, tilesX.get(0).getShape().getId(), Configuration.RANGE);
                    } else if (identityX == Identity.unspecified) {
                        listX = checkId(listX, tilesX, Identity.color, tilesX.get(0).getColor().getId(), 1);
                        listX = checkId(listX, tilesX, Identity.shape, tilesX.get(0).getShape().getId(), Configuration.RANGE);
                    }
                }
                if (!tilesY.isEmpty()) {
                    if (identityY == Identity.color) {
                        listY = checkId(listY, tilesY, identityY, tilesY.get(0).getColor().getId(), 1);
                    } else if (identityY == Identity.shape) {
                        listY = checkId(listY, tilesY, identityY, tilesY.get(0).getShape().getId(), Configuration.RANGE);
                    } else if (identityY == Identity.unspecified) {
                        listY = checkId(listY, tilesY, Identity.color, tilesY.get(0).getColor().getId(), 1);
                        listY = checkId(listY, tilesY, Identity.shape, tilesY.get(0).getShape().getId(), Configuration.RANGE);
                    }
                }
                if (listX.isEmpty() && !listY.isEmpty()) {
                    for (Tile tile : listY) {
                        if (!possibleTiles.contains(tile)) {
                            possibleTiles.add(tile);
                        }
                    }
                } else if (!listX.isEmpty() && listY.isEmpty()) {
                    for (Tile tile : listX) {
                        if (!possibleTiles.contains(tile)) {
                            possibleTiles.add(tile);
                        }
                    }
                } else {
                    for (Tile tile : listX) {
                        boolean add = false;
                        for (Tile tile2 : listY) {
                            if (tile.isEqualTo(tile2)) add = true;
                        }
                        if (add && !possibleTiles.contains(tile)) possibleTiles.add(tile);
                    }
                }
                if (!possibleTiles.isEmpty()) {
                    possibleMoves.put(loc, possibleTiles);
                }
            }
        }
        return possibleMoves;
    }

    /**
     * Checks all ID's of Tiles with a certain identity if they are in <code>tiles</code>. If not, they will be added to <code>list</code>.
     * @param list List of possible Tiles for a certain Location on the board.
     * @param tiles List of Tiles that are in a line on the board.
     * @param id ID of concerned Tile.
     * @param step Step taken by for loop that is determined by the identity.
     * @return A list with usable Tiles.
     */
    public List<Tile> checkId(List<Tile> list, List<Tile> tiles, Identity identity, int id, int step) {
        int max = 0;
        if (identity == Identity.color) {
            id = id * Configuration.RANGE;
            max = id + Configuration.RANGE;
        } else if (identity == Identity.shape) {
            max = Configuration.RANGE * Configuration.RANGE;
        }
        for (int i = id; i < max; i += step) {
            Tile temp = new Tile(i);
            boolean add = true;
            for (Tile tile : tiles) {
                if (tile.isEqualTo(temp)) add = false;
            }
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
        Identity identity;
        boolean sameColor = true;
        boolean sameShape = true;

        for (Tile tile : line) {
            for (Tile tile2 : line) {
                if (tile != tile2 && tile.getColor() != tile2.getColor()) sameColor = false;
                if (tile != tile2 && tile.getShape() != tile2.getShape()) sameShape = false;
            }
        }
        if (sameColor && !sameShape) {
            identity = Identity.color;
            outerLoop:
            for (Tile tile : line) {
                for (Tile tile2 : line) {
                    if (tile != tile2 && tile.getShape() == tile2.getShape()) {
                        identity = Identity.invalid;
                        break outerLoop;
                    }
                }
            }
        } else if (!sameColor && sameShape) {
            identity = Identity.shape;
            outerLoop:
            for (Tile tile : line) {
                for (Tile tile2 : line) {
                    if (tile != tile2 && tile.getColor() == tile2.getColor()) {
                        identity = Identity.invalid;
                        break outerLoop;
                    }
                }
            }
        } else if (sameColor /** && sameShape */){
            //This means that line only contains one tile.
            identity = Identity.unspecified;
        } else {
            identity = Identity.invalid;
        }
        return identity;
    }

    /**
     * Creates a List of Tiles that are on one line on a certain axis. The Location startPoint is not included.
     * This method uses recursion to create the List.
     * @param axis The axis on which the line lies.
     * @param location The location checked in this iteration of the method.
     * @param startPoint The start location of the method. This location is the reference point.
     * @param step The step taken on the line to the next location. Must be either 1 or -1.
     * @return A list of Tiles that lie on one line. The Location startPoint is not included.
     */
    public List<Tile> createLine(Axis axis, Location location, Location startPoint, int step) {
        List<Tile> line = new ArrayList<>();
        if (location.isEqualTo(startPoint)) {
            List<Tile> temp = axis == Axis.X ? createLine(axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                    createLine(axis, new Location(location.getX(), location.getY() + step), startPoint, step);
            line.addAll(temp);
        } else {
            for (Location loc : field.keySet()) {
                if (loc.isEqualTo(location)) {
                    line.add(field.get(loc));
                    List<Tile> temp = axis == Axis.X ? createLine(axis, new Location(location.getX() + step, location.getY()), startPoint, step) :
                            createLine(axis, new Location(location.getX(), location.getY() + step), startPoint, step);
                    line.addAll(temp);
                    break; //right location is found, further looping is unnecessary
                }
            }
            if (step > 0) {
                List<Tile> temp = axis == Axis.X ? createLine(axis, new Location(startPoint.getX() - step, startPoint.getY()), startPoint, -step) :
                        createLine(axis, new Location(startPoint.getX(), startPoint.getY() - step), startPoint, -step);
                line.addAll(temp);
            }
        }
        return line;
    }

    /**
     * Creates a List of all empty Locations next to used Locations. This List is used to determine possible moves.
     * @return A List of all empty Locations next to used Locations.
     */
    public List<Location> getOpenLocations() {
        List<Location> openLocations = new ArrayList<>();
        if (field.isEmpty()) {
            openLocations.add(new Location(0, 0));
        } else {
            for (Location loc : field.keySet()) {
                List<Location> temp = new ArrayList<>();
                temp.add(new Location(loc.getX() + 1, loc.getY()));
                temp.add(new Location(loc.getX() - 1, loc.getY()));
                temp.add(new Location(loc.getX(), loc.getY() + 1));
                temp.add(new Location(loc.getX(), loc.getY() - 1));
                for (Location loc2 : temp) {
                    boolean add = true;
                    for (Location loc3 : field.keySet()) {
                        if (loc2.isEqualTo(loc3)) add = false;
                    }
                    for (Location loc4 : openLocations) {
                        if (loc2.isEqualTo(loc4)) add = false;
                    }
                    if (add) openLocations.add(loc2);
                }
            }
        }
        return openLocations;
    }

    /**
     * Gives the Location of the with the lowest X or Y depending on axis.
     * @param axis Determines whether the Location with the lowest X or Y is returned.
     * @return Location with lowest X/Y
     */
    public Location lowerBound(Axis axis, Map<Location, Tile> map) {
        List<Location> list = new ArrayList<>();
        for (Location loc : map.keySet()) {
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
    public Location higherBound(Axis axis, Map<Location, Tile> map) {
        List<Location> list = new ArrayList<>();
        for (Location loc : map.keySet()) {
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
     * Creates a string representation of field that can be used for a TUI.
     * @return String representation of field.
     */
    public String toString() {
        if (field.isEmpty()) {
            return Configuration.EMPTY_FIELD;
        }

        int lowX = lowerBound(Axis.X, field).getX();
        int highX = higherBound(Axis.X, field).getX();
        int lowY = lowerBound(Axis.Y, field).getY();
        int highY = higherBound(Axis.Y, field).getY();
        int length = highX - lowX + 2;


        String emptyRow = "";
        String staticRow = "---|";

        for (int i = 0; i < length; i++) {
            staticRow += Configuration.MID_ROW;
            emptyRow += Configuration.EMPTY_SPACE;
        }

        staticRow += Configuration.END_ROW + "\n";
        emptyRow += Configuration.EMPTY_SPACE + "\n";

        String fieldString = staticRow + startRow(highY + 1) + emptyRow + staticRow;

        for (int j = highY; j > lowY - 1; j--) {
            String row = startRow(j) + Configuration.EMPTY_SPACE;

            Map<Location, Tile> temp = new HashMap<>();
            for (Location loc : field.keySet()) {
                if (loc.getY() == j) {
                    temp.put(loc, field.get(loc));
                }
            }
            for (int i = lowX; i <= highX; i++) {
                boolean placed = false;
                for (Location loc : temp.keySet()) {
                    if (loc.getX() == i) {
                        row += temp.get(loc) + "|";
                        placed = true;
                    }
                }
                if (!placed) {
                    row += Configuration.EMPTY_SPACE;
                }
            }
            row += Configuration.EMPTY_SPACE + "\n";
            fieldString += row + staticRow;
        }

        fieldString += startRow(lowY - 1) + emptyRow + staticRow;
        fieldString += "y/x|";
        for (int i = lowX - 1; i <= highX + 1; i++) {
            fieldString += bottomRow(i);
        }
        fieldString += "\n";
        return fieldString;
    }

    /**
     * Creates a String that is used at the start of a row that represents the Y axis.
     * @param i Coordinate on the Y axis.
     * @return The coordinate on the Y axis in a proper format.
     */
    private String startRow(int i) {
        String startRow = i < 0 ? "-" : " ";
        if (i >= - 9 && i <= 9) {
            startRow += "0";
        }
        startRow += Math.abs(i) + "|";
        return startRow;
    }

    /**
     * Creates a String that is used as the bottom row that represents the X axis.
     * @param i Coordinate on the X axis.
     * @return The bottom row of the toString() method with the coordinates of the X axis in the proper format.
     */
    private String bottomRow(int i) {
        String topRow = i < 0 ? "  -" : "   ";
        if (i >= - 9 && i <= 9) {
            topRow += "0";
        }
        topRow += Math.abs(i) + "   |";
        return topRow;
    }

}
