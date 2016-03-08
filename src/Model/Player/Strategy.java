package Model.Player;

import Model.Game.*;

import java.util.List;

/**
 * Created by Martijn on 27-1-2016.
 *
 * This interface is the basis for all Strategies that ComputerPlayers use.
 */
public interface Strategy {

    /**
     * Returns the Strategy's name.
     * @return The Strategy's name.
     */
    String getName();

    /**
     * Determines the move that is made on the board.
     * @param board The Board to be played on.
     * @param hand The hand of the ComputerPlayer that uses this Strategy.
     * @return The Move that is made.
     */
    Move determineMove(Board board, List<Tile> hand);

}
