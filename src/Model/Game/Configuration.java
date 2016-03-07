package Model.Game;

/**
 * Created by Martijn on 11-1-2016.
 *
 * This class contains a number of constant values that are used throughout the project. Containing them in a single
 * class makes for easy adjustments and, in my opinion, cleaner code.
 */
public class Configuration {

    /**
     * Amount of unique colors and shapes (not combinations)
     */
    public static final int RANGE = 6;

    /**
     * Amount of Tiles each Player gets
     */
    public static final int HAND = 6;

    /**
     * Amount of duplicates of a unique tile
     */
    public static final int TILE_DUPLICATES = 3;

    /**
     * Strings used to create a String presentation of board
     */
    public static final String EMPTY_SPACE = "        |";
    public static final String MID_ROW = "--------+";
    public static final String END_ROW = "--------|";
    public static final String EMPTY_FIELD = "|" + MID_ROW + MID_ROW + END_ROW + "\n"
            + "|" + EMPTY_SPACE + EMPTY_SPACE + EMPTY_SPACE + "\n"
            + "|" + MID_ROW + MID_ROW + END_ROW + "\n"
            + "|" + EMPTY_SPACE + EMPTY_SPACE + EMPTY_SPACE + "\n"
            + "|" + MID_ROW + MID_ROW + END_ROW + "\n"
            + "|" + EMPTY_SPACE + EMPTY_SPACE + EMPTY_SPACE + "\n"
            + "|" + MID_ROW + MID_ROW + END_ROW + "\n";

}
