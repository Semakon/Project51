package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * Created by Martijn on 22-1-2016.
 *
 * This extension of Player only makes moves that are given to it. It's used to simulate actual players on the
 * ServerGame and ClientGame (otherPlayers).
 */
public class SocketPlayer extends Player {

    /**
     * The current Move.
     */
    private Move currentMove;

    /**
     * The previous Move.
     */
    private Move previousMove;

    /**
     * Creates a new instance of Player with a name and a hand, that contains exactly Configuration.HAND Tiles.
     *
     * @param name The player's name.
     * @param hand The player's Tiles.
     */
    public SocketPlayer(String name, List<Tile> hand) {
        super(name, hand);
    }

    /**
     * Sets the currentMove to a new currentMove.
     * @param currentMove The new currentMove.
     */
    public void setCurrentMove(Move currentMove) {
        this.currentMove = currentMove;
    }

    /**
     * Returns the previous Move.
     * @return The previous Move.
     */
    public Move getPreviousMove() {
        return previousMove;
    }

    /**
     * Returns the currentMove and resets it.
     * @param board the current board.
     * @return The currentMove.
     * @throws InvalidMoveException When there currentMove is not set.
     */
    @Override
    public Move determineMove(Board board) throws InvalidMoveException {
        // If there was no move made, throw exception
        if (currentMove == null) throw new InvalidMoveException("No move was made.");

        // Set the previous move as the current move and reset the current move
        previousMove = currentMove;
        currentMove = null;

        // return the move that was made.
        return previousMove;
    }
}
