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


//-----------------------------Sub tests (getOpenLocations(), createLine(), getIdentity(), checkId():-----------------------------

//        List<Location> openLocs = b.getOpenLocations();
//        System.out.println(openLocs);
//        System.out.println(openLocs.size());
//
//        for (Location loc : openLocs) {
//            System.out.println(b.toString() + "\n");
//            List<Tile> tilesX = b.createLine(Axis.X, loc, loc, 1);
//            List<Tile> tilesY = b.createLine(Axis.Y, loc, loc, 1);
//            Identity identityX = b.getIdentity(tilesX);
//            Identity identityY = b.getIdentity(tilesY);
//
//            System.out.println(loc + ":");
//            System.out.println("X:\t\t\t" + tilesX);
//            System.out.println("IdentityX:\t" + identityX);
//            System.out.println("Y:\t\t\t" + tilesY);
//            System.out.println("IdentityY:\t" + identityY);
//
//            if (tilesX.size() < 6 && identityX != Identity.invalid && tilesY.size() < 6 && identityY != Identity.invalid) {
//                List<Tile> possibleTiles = new ArrayList<>();
//                List<Tile> listX = new ArrayList<>();
//                List<Tile> listY = new ArrayList<>();
//
//                if (!tilesX.isEmpty()) {
//                    if (identityX == Identity.color) {
//                        listX = b.checkId(listX, tilesX, identityX, tilesX.get(0).getColor().getId(), 1);
//                    } else if (identityX == Identity.shape) {
//                        System.out.println(tilesX.get(0).getShape().getId());
//                        listX = b.checkId(listX, tilesX, identityX, tilesX.get(0).getShape().getId(), Configuration.RANGE);
//                    } else if (identityX == Identity.unspecified) {
//                        listX = b.checkId(listX, tilesX, Identity.color, tilesX.get(0).getColor().getId(), 1);
//                        listX = b.checkId(listX, tilesX, Identity.shape, tilesX.get(0).getShape().getId(), Configuration.RANGE);
//                    }
//                    System.out.println("listX:\t\t" + listX);
//                }
//                if (!tilesY.isEmpty()) {
//                    if (identityY == Identity.color) {
//                        listY = b.checkId(listY, tilesY, identityY, tilesY.get(0).getColor().getId(), 1);
//                    } else if (identityY == Identity.shape) {
//                        listY = b.checkId(listY, tilesY, identityY, tilesY.get(0).getShape().getId(), Configuration.RANGE);
//                    } else if (identityY == Identity.unspecified) {
//                        listY = b.checkId(listY, tilesY, Identity.color, tilesY.get(0).getColor().getId(), 1);
//                        listY = b.checkId(listY, tilesY, Identity.shape, tilesY.get(0).getShape().getId(), Configuration.RANGE);
//                    }
//                    System.out.println("listY:\t\t" + listY);
//                }
//                if (listX.isEmpty() && !listY.isEmpty()) {
//                    listY.stream().filter(tile -> !possibleTiles.contains(tile)).forEach(possibleTiles::add);
//                } else if (!listX.isEmpty() && listY.isEmpty()) {
//                    listX.stream().filter(tile -> !possibleTiles.contains(tile)).forEach(possibleTiles::add);
//                } else {
//                    for (Tile tile : listX) {
//                        boolean add = false;
//                        for (Tile tile2 : listY) {
//                            if (tile.isEqualTo(tile2)) add = true;
//                        }
//                        if (add && !possibleTiles.contains(tile)) possibleTiles.add(tile);
//                    }
//                }
//                System.out.println("Tiles:\t\t" + possibleTiles + "\nSize:\t\t" + possibleTiles.size() + "\n");
//            }
//        }

    }



}
