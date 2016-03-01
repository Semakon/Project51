package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * Created by Martijn on 22-1-2016.
 */
public class SocketPlayer extends Player {

    private Move currentMove;
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

    public void setCurrentMove(Move currentMove) {
        this.currentMove = currentMove;
    }

    public Move getPreviousMove() {
        return previousMove;
    }

    @Override
    public Move determineMove(Board board) throws InvalidMoveException {
        // If there was no move made, throw exception
        if (currentMove == null) throw new InvalidMoveException("No move was made.");

        // Set the previous move as the current move and set the current move to null
        previousMove = currentMove;
        currentMove = null;

        // return the move that was made.
        return previousMove;
    }
}
