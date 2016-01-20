package Model.Game;

import Model.Player.Player;

/**
 * Created by martijn on 11-1-16.
 */
public class Game {

    private Board board;
    private Pool pool;
    private Player[] players;
    private int currentPlayer;

    public Game(Player[] players, int firstMove) {
        pool = new Pool();
        board = new Board();
        this.players = players;
        this.currentPlayer = firstMove;
    }

    public Board getBoard() {
        return board;
    }

    public Pool getPool() {
        return pool;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

}
