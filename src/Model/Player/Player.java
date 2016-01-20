package Model.Player;

import Model.Game.Board;
import Model.Game.Move;
import View.View;

/**
 * Created by Martijn on 11-1-2016.
 */
public abstract class Player {

    private String name;
    private View UI;

    /**
     * Creates a new instance of Player with a name and a UI.
     * @param name the player's name.
     * @param UI the UI.
     */
    public Player(String name, View UI) {
        this.name = name;
        this.UI = UI;
    }

    /**
     * returns the player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * returns the View.
     */
    public View getUI() {
        return UI;
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
        Move move = null;
        boolean valid = false;
        while (!valid) {
            move = determineMove(board);
            if (board.validMove(move)) {
                valid = true;
            } else {
                UI.showError("That move is invalid.");
            }
        }
        return move;
    }

}
