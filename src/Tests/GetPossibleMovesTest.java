package Tests;

import Model.Game.Board;
import Model.Game.Configuration;
import Model.Game.Enumerations.Axis;
import Model.Game.Enumerations.Identity;
import Model.Game.Location;
import Model.Game.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Martijn on 26-1-2016.
 */
public class GetPossibleMovesTest {

    public static void main(String[] args) {
        Board b = new Board();

        b.getField().put(new Location(0, 0), new Tile(31));
        b.getField().put(new Location(1, 0), new Tile(30));
        b.getField().put(new Location(2, 0), new Tile(32));

        b.getField().put(new Location(1, 2), new Tile(18));
        b.getField().put(new Location(1, 1), new Tile(24));
        b.getField().put(new Location(1, 3), new Tile(6));

        b.getField().put(new Location(0, -1), new Tile(13));
        b.getField().put(new Location(-1, -1), new Tile(14));

        Map<Location, List<Tile>> possibleMoves = b.getPossibleMoves();
        for (Location loc : possibleMoves.keySet()) {
            System.out.println(b.toString() + "\n");
            System.out.println(loc + ":\nTiles:\t" + possibleMoves.get(loc) + "\nSize:\t" + possibleMoves.get(loc).size() + "\n");
        }

    }



}
