package Model.Game;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Game.Exceptions.InvalidAmountRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents the pool where all Tiles are drawn from. Every Tile used in the game originally came from
 * this class.
 *
 * Created by Martijn on 12-1-2016.
 */
public class Pool {

    /**
     * The actual list of Tiles that are still contained in the pool.
     */
    private List<Tile> pool;

    /**
     * Creates a new instance of Pool with Configuration.RANGE * Configuration.RANGE unique tiles. Every unique tile appears
     * Configuration.TILE_DUPLICATES times in the pool.
     */
    public Pool() {
        // initialize pool
        pool = new ArrayList<>();

        // fill the pool with Tiles
        for (int i = 0; i < Configuration.TILE_DUPLICATES; i++) {
            for (int j = 0; j < Configuration.RANGE * Configuration.RANGE; j++) {
                pool.add(new Tile(j));
            }
        }
    }

    /**
     * Returns the actual list of Tiles contained in this pool.
     * @return The pool.
     */
    public /*@ pure @*/ List<Tile> getPool() {
        return pool;
    }

    /**
     * Checks whether pool is empty.
     * @return True if pool has no Tiles left.
     */
    public /*@ pure @*/ boolean isEmpty() {
        return pool.isEmpty();
    }

    /**
     * Takes a specified amount of tiles out of the pool. The amount must be between 0 (exclusive) and Configuration.MAXIMUM_HAND (inclusive).
     * @param amount tiles taken from the pool
     * @return a List of Tiles
     * @throws InsufficientTilesInPoolException If the pool doesn't have enough tiles to take
     */
    /*@
        requires 0 <= amount && amount <= 6 && getPool() >= amount && getPool() != null;
        ensures \result != null;
     */
    public List<Tile> takeTiles(int amount) throws InsufficientTilesInPoolException {

        // check whether amount is valid
        if (amount <= 0 || amount > Configuration.HAND) {
            throw new InvalidAmountRuntimeException("Amount must be between 0 (exclusive) and " + Configuration.HAND + " (inclusive).");
        }

        // check whether pool contains enough tiles to take
        if (pool.size() < amount) {
            throw new InsufficientTilesInPoolException();
        }

        // initialize return list and random generator
        List<Tile> tiles = new ArrayList<>();
        Random randomGen = new Random();

        // take <amount> tiles out of the pool randomly and put them in the return list
        for (int i = 0; i < amount; i++) {
            int index = randomGen.nextInt(pool.size());
            tiles.add(pool.get(index));
            pool.remove(index);
        }
        return tiles;
    }

    /**
     * Trades the oldTiles with random tiles from the pool.
     * @param oldTiles Tiles to be traded
     * @return a List of new Tiles from the pool
     * @throws InsufficientTilesInPoolException If the pool doesn't have enough tiles to trade
     */
    /*@
        requires oldTiles != null && 0 <= oldTiles.size() && oldTiles.size() <= 6;
        ensures \result != null;
     */
    public List<Tile> tradeTiles(List<Tile> oldTiles) throws InsufficientTilesInPoolException {

        // get correct amount of tiles from the pool
        List<Tile> newTiles = takeTiles(oldTiles.size());

        // add all old tiles to the pool
        for (Tile oldTile : oldTiles) pool.add(oldTile);

        return newTiles;
    }

}
