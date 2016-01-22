package Model.Game;

import Model.Player.Player;

/**
 * Created by martijn on 11-1-16.
 */
public class Game {

    private Board board;
    private Player[] players;
    private int currentPlayer;

    public Game(String[] players, String firstMove) {
        board = new Board();
        this.players = new Player[players.length];
        //TODO: create players
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

}
