package Model.Player;

import Model.Game.Board;
import Model.Game.Exceptions.InvalidMoveException;
import Model.Game.Move;
import Model.Game.Tile;

import java.util.List;

/**
 * Created by Martijn on 11-1-2016.
 */
public abstract class Player {

    private String name;
    private List<Tile> hand;

    /**
     * Creates a new instance of Player with a name and a UI.
     * @param name the player's name.
     */
    public Player(String name, List<Tile> hand) {
        this.name = name;
        this.hand = hand;
    }

    /**
     * returns the player's name.
     */
    public String getName() {
        return name;
    }

    public List<Tile> getHand() {
        return hand;
    }

    /**
     * Determines the next move.
     * @param board the current board.
     * @return the player's choice.
     */
    public abstract Move determineMove(Board board);

    /**
     * Makes a move on the board.
     * @param board the current board.
     */
    public Move makeMove(Board board) {
        return determineMove(board); //TODO: merge with determineMove (?)
    }

}
