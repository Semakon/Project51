package Model.Game;

/**
 * This class represents the location of a Tile on the board. It has a unique X and Y coordinate combination.
 *
 * Created by Martijn on 11-1-2016.
 */
public class Location {

    /**
     * This Location's X coordinate.
     */
    private int x;

    /**
     * This Location's Y coordinate.
     */
    private int y;

    /**
     * Creates a new instance of a Locations with an X and a Y coordinate.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns this Location's X coordinate.
     * @return This Location's X coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns this Location's Y coordinate.
     * @return This Location's Y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Checks whether another location has the same X and Y coordinate.
     * @param loc the other Location.
     * @return True if loc points to the same location as this Location.
     */
    public boolean equals(Location loc) {
        return equals(loc.getX(), loc.getY());
    }

    /**
     * Checks whether a given X and Y are equal to this Location's X and Y.
     * @param x X coordinate to be tested.
     * @param y Y coordinate to be tested.
     * @return True if X and Y are equal to this.x and this.y respectively.
     */
    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    /**
     * Gives a String representation of Location for the IProtocol.
     * @return (X, Y)
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
