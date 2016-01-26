package Model.Game;

/**
 * Created by Martijn on 11-1-2016.
 */
public class Location {

    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Checks whether another location has the same x and y
     * @param loc the other Location
     * @return True if loc points to the same location as this Location
     */
    public boolean isEqualTo(Location loc) {
        return this.x == loc.getX() && this.y == loc.getY();
    }

    /**
     * Checks whether a given x and y are equal to this Location's x and y.
     * @param x x to be tested
     * @param y y to be tested
     * @return True if x and y are equal to this.x and this.y respectively
     */
    public boolean isEqualTo(int x, int y) {
        return this.x == x && this.y == y;
    }

    /**
     * Gives a String representation of Location for the IProtocol.
     * @return x,y
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
