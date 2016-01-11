package Model.Game;

/**
 * Created by martijn on 11-1-16.
 */
public class Game {

    private Board board;
    private Player[] players;
    private int currentPlayer;
    private int score;

    public Game() {

    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    public int getScore() {
        return score;
    }

}
