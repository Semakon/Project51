package Model.Game;

import Model.Game.Enumerations.Color;
import Model.Game.Enumerations.Shape;
import Model.Game.Exceptions.InvalidIdRuntimeException;

/**
 * Created by martijn on 11-1-16.
 */
public class Tile {

    private int id;
    private Color color;
    private Shape shape;

    /**
     * Creates a new Tile with an id, color and shape.
     * @param id The Tile's id.
     * @throws InvalidIdRuntimeException If one tries to create a Tile with an invalid id.
     */
    public Tile(int id) {
        if (id < 0 || id > 35) {
            throw new InvalidIdRuntimeException("ID must be between 0 (inclusive) and " + (Configuration.RANGE * Configuration.RANGE) + " (exclusive).");
        }
        this.id = id;
        this.color = idToColor(id);
        this.shape = idToShape(id);
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }

    /**
     * Checks whether a Tile is equal to this Tile.
     * @param tile Tile to be checked.
     * @return True if this Tile and tile have the same shape and color.
     */
    public boolean isEqualTo(Tile tile) {
        return tile.getId() == id;
    }

    /**
     * Divides the id of this tile by RANGE and uses the outcome to give the color of this tile.
     * @return Color of this Tile.
     */
    private Color idToColor(int id) {
        id = id / Configuration.RANGE;
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
        id = id % Configuration.RANGE;
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
     * Gives a representation of a Tile.
     * @return ([color], [shape])
     */
    public String toString() {
        return "(" + this.color + ", " + this.shape + ")";
    }

}
