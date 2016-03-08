package Model.Game.Enumerations;

/**
 * This enumeration represents the colors the tiles can have.
 *
 * Created by Martijn on 13-1-2016.
 */
public enum Color {

    Green, Yellow, Red, Purple, Orange, Blue;

    /**
     * Returns the color's corresponding id. The default id is -1.
     * @return this color's corresponding id.
     */
    public int getId() {
        int id = -1;
        switch (this) {
            case Green: id = 0;
                break;
            case Yellow: id = 1;
                break;
            case Red: id = 2;
                break;
            case Blue: id = 3;
                break;
            case Purple: id = 4;
                break;
            case Orange: id = 5;
                break;
        }
        return id;
    }

    /**
     * Translates the enumeration into a String that can be used in the toString() of the Board class.
     * "XX" is the default.
     * @return A String representation of this color.
     */
    public String toString() {
        String color = "XX";
        switch (this) {
            case Green: color = "Gr";
                break;
            case Yellow: color = "Ye";
                break;
            case Orange: color = "Or";
                break;
            case Blue: color = "Bl";
                break;
            case Red: color = "Re";
                break;
            case Purple: color = "Pu";
        }
        return color;
    }

}
