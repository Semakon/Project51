package Tests;

import Model.Game.Tile;

/**
 * Created by martijn on 11-1-16.
 */
public class TileTest {

    public static void main(String args[]) {
        Tile tile1 = new Tile(0);
        System.out.println(tile1.getId());
        System.out.println(tile1.getColor());
        System.out.println(tile1.getShape());
        System.out.println(tile1 + "\n");

        Tile tile2 = new Tile(32);
        System.out.println(tile2.getId());
        System.out.println(tile2.getColor());
        System.out.println(tile2.getShape());
        System.out.println(tile2 + "\n");

        Tile tile3 = new Tile(20);
        System.out.println(tile3.getId());
        System.out.println(tile3.getColor());
        System.out.println(tile3.getShape());
        System.out.println(tile3);
    }

}
