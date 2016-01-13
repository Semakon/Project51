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
     * Determines whether another location has the same x and y
     * @param loc the other Location
     * @return true if loc points to the same location as this Location
     */
    public boolean isDuplicate(Location loc) {
        return this.x == loc.getX() && this.y == loc.getY();
    }

    /**
     * Gives a String representation of Location for the IProtocol.
     * @return x,y
     */
    public String toString() {
        return x + "," + y;
    }

}
