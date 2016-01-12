package Model.Game;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Game.Exceptions.InvalidAmountRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Martijn on 12-1-2016.
 */
public class Pool {

    private List<Tile> pool;

    /**
     * Creates a new instance of Pool. TO BE CONTINUED!
     */
    public Pool() {
        pool = new ArrayList<>();
        for (int i = 0; i < Configuration.TILE_DUPLICATES; i++) {
            for (int j = 0; j < Configuration.DIVISION * Configuration.DIVISION; j++) {
                pool.add(new Tile(j));
            }
        }
    }

    public List<Tile> getPool() {
        return pool;
    }

    /**
     * Takes a specified amount of tiles out of the pool. The amount must be between 0 (exclusive) and Configuration.MAXIMUM_HAND (inclusive).
     * @param amount tiles taken from the pool
     * @return a List of Tiles
     * @throws InsufficientTilesInPoolException If the pool doesn't have enough tiles to take
     */
    public List<Tile> takeTiles(int amount) throws InsufficientTilesInPoolException {
        if (amount <= 0 || amount > Configuration.MAXIMUM_HAND) {
            throw new InvalidAmountRuntimeException("Amount must be between 0 (exclusive) and " + Configuration.MAXIMUM_HAND + " (inclusive).");
        }
        if (pool.size() < amount) {
            throw new InsufficientTilesInPoolException("Pool doesn't contain enough tiles.");
        }

        List<Tile> tiles = new ArrayList<>();
        Random randomGen = new Random();
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
    public List<Tile> tradeTiles(List<Tile> oldTiles) throws InsufficientTilesInPoolException {
        if (pool.size() < oldTiles.size()) {
            throw new InsufficientTilesInPoolException("Pool doesn't contain enough tiles.");
        }

        List<Tile> newTiles = new ArrayList<>();
        Random randomGen = new Random();
        for (int i = 0; i < oldTiles.size(); i++) {
            int index = randomGen.nextInt(pool.size());
            newTiles.add(pool.get(index));
            pool.remove(index);
        }

        for (Tile oldTile : oldTiles) {
            pool.add(oldTile);
        }
        return newTiles;
    }

}
