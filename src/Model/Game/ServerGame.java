package Model.Game;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Player.Player;
import Model.Player.SocketPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martijn on 11-1-16.
 */
public class ServerGame {

    private Board board;
    private List<Player> players;
    private Player currentPlayer;
    private Pool pool;

    /**
     * Creates an instance of Game. This can be on a Server or on a Client and the behaviour changes accordingly.
     * @param players Array of names of players.
     * @param firstMove Name of player with the first move.
     */
    public ServerGame(String[] players, String firstMove) {
        board = new Board();
        pool = new Pool();
        this.players = new ArrayList<>();
        try {
            for (String player : players) {
                this.players.add(new SocketPlayer(player, pool.takeTiles(Configuration.HAND)));
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

    public Pool getPool() {
        return pool;
    }

    public void play() {
        boolean done = false;
        while (!done) {
            currentPlayer.makeMove(board);
            if (gameOver()) {

                done = true;
            }
        }
    }

    public void madeTradeMove(String player, int[] move) {

    }

    public void madePutMove(String player, List<int[]> move) { //move[i]: {x, y, id}
        Map<Location, Tile> moveSet = new HashMap<>();
        for (int[] m : move) {
            moveSet.put(new Location(m[0], m[1]), new Tile(m[2]));
        }
        PutMove putMove = new PutMove(moveSet);
        for (Player p : players) {
            if (player.equals(p.getName())) {
                // have p make putMove move.
            }
        }
    }

    /**
     * Checks whether the game is over according to the game's rules. A game is over when
     * - There are no longer any possible moves.
     * - The pool is empty and at least one player's hand is also empty.
     * @return True if the game is over according to the game's rules.
     */
    public boolean gameOver() {
        boolean gameOver = false;
        for (Player p : players) {
            if (p.getHand().isEmpty() && pool.isEmpty()) {
                gameOver = true;
            }
        }
        if (board.getPossibleMoves().isEmpty()) gameOver = true;
        return gameOver;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Gets the hand of the player with name <code>player</code>.
     * @param player The player's name.
     * @return Hand of player with name <code>player</code>.
     */
    public List<Tile> getHand(String player) {
        List<Tile> hand = null;
        for (Player p : players) {
            if (player.equals(p.getName())) hand = p.getHand();
        }
        return hand;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() { return players;}

}
