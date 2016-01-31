package Model.Player;

import Model.Game.*;

import java.util.List;

/**
 * Created by Martijn on 27-1-2016.
 */
public interface Strategy {

    String getName();
    Move determineMove(Board board, List<Tile> hand);


}
