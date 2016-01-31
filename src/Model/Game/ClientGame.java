package Model.Game;

import Model.Player.Player;
import Model.Player.Strategy;

import java.util.List;

/**
 * Created by Martijn on 30-1-2016.
 */
public class ClientGame {

    private Board board;
    private String[] players;
    private Player thisPlayer;
    private String currentPlayer;

    public ClientGame(Strategy strategy, String thisPlayer, String[] players) {
        board = new Board();
        this.players = players;
        if (strategy == null) {
            //human player
        } else {
            //computer player
        }
    }

    public void update() {
        //Player made a move.
    }

    public void play() {

    }

    public void madeMove(Move move) {

    }

    public Board getBoard() {
        return board;
    }

    /**
     * Gets the hand of the player.
     * @return Hand of player.
     */
    public List<Tile> getHand() {
        return thisPlayer.getHand();
    }

    /**
     * Sets the current player to the player with name <code>name</code>.
     * @param name Name of new current player.
     */
    public void setCurrentPlayer(String name) {
        this.currentPlayer = name;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

}
