package Model.Game;

import Model.Player.Player;

/**
 * Created by martijn on 11-1-16.
 */
public class Game {

    private Board board;
    private Player[] players;
    private int currentPlayer;

    public Game(Player[] players, int firstMove) {
        board = new Board();
        this.players = players;
        this.currentPlayer = firstMove;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

}
