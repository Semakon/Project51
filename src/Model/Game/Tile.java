package Model.Game;

import Model.Game.Enumerations.Color;
import Model.Game.Enumerations.Shape;
import Model.Game.Exceptions.InvalidIdRuntimeException;

/**
 * This class represents a tile that is used to play the game. A Tile has a few characteristics and utilities to
 * compare tiles and represent them on the board.
 *
 * Created by martijn on 11-1-16.
 */
public class Tile {

    /**
     * The ID of a Tile that is used to construct it and decide its color and shape.
     */
    private int id;

    /**
     * The color of the Tile.
     */
    private Color color;

    /**
     * The shape of the Tile.
     */
    private Shape shape;

    /**
     * Creates a new Tile with an id, color and shape.
     * @param id The Tile's id.
     * @throws InvalidIdRuntimeException If one tries to create a Tile with an invalid id.
     */
    public Tile(int id) {

        // check whether id is valid
        if (id < 0 || id > 35) {
            throw new InvalidIdRuntimeException("ID must be between 0 (inclusive) and " +
                    (Configuration.RANGE * Configuration.RANGE) + " (exclusive).");
        }

        // initialize fields
        this.id = id;
        this.color = idToColor(id);
        this.shape = idToShape(id);
    }

    /**
     * Returns this Tile's ID.
     * @return This Tile's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns this Tile's color.
     * @return This Tile's color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns this Tile's shape.
     * @return This Tile's shape.
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Checks whether a Tile is equal to this Tile.
     * @param tile Tile to be checked.
     * @return True if this Tile and tile have the ID and therefore the same shape and color.
     */
    public boolean equals(Tile tile) {
        return tile.getId() == id;
    }

    /**
     * Divides the id of this tile by RANGE and uses the outcome to give the color of this tile.
     * @param id The Tile's ID.
     * @return Color of this Tile.
     */
    private Color idToColor(int id) {
        // transmute id to fit the ID of the Tile's color
        id = id / Configuration.RANGE;

        // return color corresponding with transmuted id
        switch (id) {
            case 0 : return Color.Green;
            case 1 : return Color.Yellow;
            case 2 : return Color.Red;
            case 3 : return Color.Blue;
            case 4 : return Color.Purple;
            case 5 : return Color.Orange;
            default : return null;
        }
    }

    /**
     * Divides the id of this tile by RANGE and uses the outcome to give the shape of this tile.
     * @return Shape of this Tile.
     */
    private Shape idToShape(int id) {
        // transmute id to fit the ID of the Tile's shape
        id = id % Configuration.RANGE;

        // return shape corresponding with transmuted id
        switch (id) {
            case 0 : return Shape.Cross;
            case 1 : return Shape.Plus;
            case 2 : return Shape.Square;
            case 3 : return Shape.Star;
            case 4 : return Shape.Circle;
            case 5 : return Shape.Diamond;
            default : return null;
        }
    }

    /**
     * Gives a String representation of this Tile.
     * @return ([color], [shape])
     */
    public String toString() {
        return "(" + this.color + ", " + this.shape + ")";
    }

}
