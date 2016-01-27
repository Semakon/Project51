package Model.Game;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Player.HumanPlayer;
import Model.Player.Player;
import Model.Player.SocketPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martijn on 11-1-16.
 */
public class Game {

    private Board board;
    private List<Player> players;
    private String currentPlayer;

    /**
     * Creates an instance of Game. This can be on a Server or on a Client and the behaviour changes accordingly.
     * @param thisPlayer The player of this client. Null if this instance of Game runs on a Server. Is not present in players.
     * @param players Array of names of players. Does not contain thisPlayer.
     * @param firstMove Name of player with the first move.
     */
    public Game(String thisPlayer, String[] players, String firstMove) {
        board = new Board();
        this.players = new ArrayList<>();
        try {
            if (thisPlayer == null) {
                //Game is on Server
                for (String player : players) {
                    this.players.add(new SocketPlayer(player, board.getPool().takeTiles(Configuration.HAND)));
                }
            } else {
                //Game is on Client
                this.players.add(new HumanPlayer(thisPlayer, board.getPool().takeTiles(Configuration.HAND)));
                for (String player : players) {
                    this.players.add(new SocketPlayer(player, board.getPool().takeTiles(Configuration.HAND)));
                }
            }
            currentPlayer = firstMove;
        } catch (InsufficientTilesInPoolException e) {
            //should not occur
            e.printStackTrace();
            System.exit(0);
        }
    }

    public String update() {
        return board.toString();
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        Player player = null;
        for (Player p : players) {
            if (currentPlayer.equals(p.getName())) player = p;
        }
        return player;
    }

}
