package Model.Game;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Player.ComputerPlayer;
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
    private Player currentPlayer;

    /**
     * Creates an instance of Game. This can be on a Server or on a Client and the behaviour changes accordingly.
     * @param thisPlayer The player of this client. Null if this instance of Game runs on a Server. Is not present in players.
     * @param players Array of names of players. Does not contain thisPlayer.
     * @param firstMove Name of player with the first move.
     */
    public Game(String thisPlayer, Strategy strategy, String[] players, String firstMove) {
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
                if (strategy == null) {
                    this.players.add(new HumanPlayer(thisPlayer, board.getPool().takeTiles(Configuration.HAND)));
                } else {
                    this.players.add(new ComputerPlayer(strategy, board.getPool().takeTiles(Configuration.HAND)));
                }
                for (String player : players) {
                    this.players.add(new SocketPlayer(player, board.getPool().takeTiles(Configuration.HAND)));
                }
            }
            for (Player p : this.players) {
                if (firstMove.equals(p.getName())) currentPlayer = p;
            }
        } catch (InsufficientTilesInPoolException e) {
            //should not occur
            e.printStackTrace();
            System.exit(0);
        }
    }

    public String update() {
        return board.toString();
    }

    public void play() {
        boolean done = false;
        while (!done) {
            getCurrentPlayer().makeMove(board);
            if (gameOver()) {

            }
        }
    }

    public boolean gameOver() {
        boolean gameOver = false;
        for (Player p : players) {
            if (p.getHand().isEmpty() && board.getPool().isEmpty()) {
                gameOver = true;
            }
        }
        if (board.getPossibleMoves().isEmpty()) gameOver = true;
        return gameOver;
    }

    public Board getBoard() {
        return board;
    }

    public List<Tile> getHand(String player) {
        List<Tile> hand = null;
        for (Player p : players) {
            if (player.equals(p.getName())) hand = p.getHand();
        }
        return hand;
    }

    /**
     *
     * @param player
     */
    public void setCurrentPlayer(String player) {
        Player currentPlayer = null;
        for (Player p : players) {
            if (player.equals(p.getName())) currentPlayer = p;
        }
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() { return players;}

}
