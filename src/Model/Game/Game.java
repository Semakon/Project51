package Model.Game;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Player.HumanPlayer;
import Model.Player.Player;
import Model.Player.SocketPlayer;

/**
 * Created by Martijn on 11-1-16.
 */
public class Game {

    private Board board;
    private Player[] players;
    private int currentPlayer;

    /**
     * Creates an instance of Game. This can be on a Server or on a Client and the behaviour changes accordingly.
     * @param thisPlayer The player of this client. Null if this instance of Game runs on a Server. Is not present in players.
     * @param players Array of names of players. Does not contain thisPlayer.
     * @param firstMove Name of player with the first move.
     */
    public Game(String thisPlayer, String[] players, String firstMove) {
        board = new Board();
        try {
            if (thisPlayer == null) {
                //Game is on Server
                this.players = new SocketPlayer[players.length];
                for (int i = 0; i < this.players.length; i++) {
                    this.players[i] = new SocketPlayer(players[i], board.getPool().takeTiles(Configuration.HAND));
                }
            } else {
                //Game is on Client
                this.players = new Player[players.length + 1];
                this.players[0] = new HumanPlayer(thisPlayer, board.getPool().takeTiles(Configuration.HAND));
                for (int i = 0; i < players.length; i++) {
                    this.players[i] = new SocketPlayer(players[i], board.getPool().takeTiles(Configuration.HAND));
                }
            }
            for (int i = 0; i < players.length; i++) {
                if (this.players[i].getName().equals(firstMove)) {
                    currentPlayer = i;
                    break;
                }
            }
        } catch (InsufficientTilesInPoolException e) {
            //should not occur
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

}
