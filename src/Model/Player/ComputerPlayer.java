package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * Created by Martijn on 27-1-2016.
 */
public class ComputerPlayer extends Player {

    private Strategy strategy;

    /**
     * Creates a new instance of Player with a name and a hand, that contains exactly Configuration.HAND Tiles.
     *
     * @param strategy The computer's strategy.
     * @param hand The player's Tiles.
     */
    public ComputerPlayer(Strategy strategy, List<Tile> hand) {
        super(strategy.getName(), hand);
        this.strategy = strategy;
    }

    @Override
    public Move determineMove(Board board) throws InvalidMoveException {
        return strategy.determineMove(board, getHand());
    }
}
