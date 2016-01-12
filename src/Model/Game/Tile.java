package Model.Game;

import Model.Game.Exceptions.InvalidIdRunTimeException;

/**
 * Created by martijn on 11-1-16.
 */
public class Tile {

    private int id;
    private String color;
    private String shape;

    /**
     * Creates a new Tile with an id, color and shape.
     * @param id The Tile's id.
     */
    public Tile(int id) {
        if (id < 0 || id > 35) {
            throw new InvalidIdRunTimeException("ID must be between 0 (inclusive) and " + (Configuration.DIVISION * Configuration.DIVISION) + ".");
        }
        this.id = id;
        this.color = IdToColor();
        this.shape = IdToShape();
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getShape() {
        return shape;
    }

    /**
     * Divides the id of this tile by DIVISION and uses the outcome to give the color of this tile in a String presentation.
     * @return String presentation of this tile's color.
     */
    private String IdToColor() {
        String color;
        int colorId = id / Configuration.DIVISION;
        switch (colorId) {
            case 0 : color = "Green";
                break;
            case 1 : color = "Yellow";
                break;
            case 2 : color = "Red";
                break;
            case 3 : color = "Blue";
                break;
            case 4 : color = "Purple";
                break;
            case 5 : color = "Orange";
                break;
            default : color = null;
                break;
        }
        return color;
    }

    /**
     * Divides the id of this tile by DIVISION and uses the outcome to give the shape of this tile in a String presentation.
     * @return String presentation of this tile's shape.
     */
    private String IdToShape() {
        String shape;
        int colorId = id % Configuration.DIVISION;
        switch (colorId) {
            case 0 : shape = "Cross";
                break;
            case 1 : shape = "Plus";
                break;
            case 2 : shape = "Square";
                break;
            case 3 : shape = "Star";
                break;
            case 4 : shape = "Circle";
                break;
            case 5 : shape = "Diamond";
                break;
            default : shape = null;
                break;
        }
        return shape;
    }

}
