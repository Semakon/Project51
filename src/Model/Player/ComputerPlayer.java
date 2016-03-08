package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * This class represents a Player controlled by the computer.
 * The computer uses a Strategy to actually make the decisions.
 *
 * Created by Martijn on 27-1-2016.
 */
public class ComputerPlayer extends Player {

    /**
     * The Strategy the computer uses to determine its moves.
     */
    private Strategy strategy;

    /**
     * Creates a new instance of Player with a name and a hand, that contains exactly Configuration.HAND Tiles.
     * @param strategy The computer's strategy.
     * @param hand The player's Tiles.
     */
    public ComputerPlayer(Strategy strategy, List<Tile> hand) {
        super(strategy.getName(), hand);
        this.strategy = strategy;
    }

    /**
     * Determine the move that the player will make.
     * @param board the current board.
     * @return The Move this Player makes.
     * @throws InvalidMoveException When the move that the Player attempts to make is invalid.
     */
    @Override
    public Move determineMove(Board board) throws InvalidMoveException {
        return strategy.determineMove(board, getHand());
    }
}
