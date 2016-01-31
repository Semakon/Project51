package Tests;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Game.Exceptions.InvalidAmountRuntimeException;
import Model.Game.Pool;
import Model.Game.Tile;

import java.util.List;

/**
 * Created by Martijn on 20-1-2016.
 */
public class PoolTest {

    public static void main(String[] args) {
        Pool pool = new Pool();
        System.out.println("Pool: " + pool.getPool());
        System.out.println(pool.getPool().size() + "\n");
        try {
            List<Tile> list = pool.takeTiles(6);
            System.out.println("Taken tiles: " + list + "\n");
            list = pool.tradeTiles(list);
            System.out.println("Traded tiles: " + list + "\n");
            list = pool.tradeTiles(list);
            System.out.println("Traded tiles: " + list + "\n");
            list = pool.tradeTiles(list);
            System.out.println("Traded tiles: " + list + "\n");
        } catch (InvalidAmountRuntimeException e) {
            System.out.println("invalidAmount");
            e.getMessage();
        } catch (InsufficientTilesInPoolException e) {
            System.out.println("\n" + e.getMessage() + "\n");
        }
        System.out.println("Pool: " + pool.getPool());
        System.out.println(pool.getPool().size() + "\n");
    }

}
