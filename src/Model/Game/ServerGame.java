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
    private List<SocketPlayer> players;
    private Player currentPlayer;
    private Pool pool;

    /**
     * Creates an instance of ServerGame. Assigns initial values to the fields.
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
            for (SocketPlayer p : this.players) {
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
                //TODO: implement further
                done = true;
            }
        }
    }

    /**
     * This method is called when a TradeMove is made with the player that made the move and the move itself as an int
     * array as parameters. It sets the corresponding SocketPlayer's currentMove to the move that was made.
     * @param player The player that made the move.
     * @param move The move that was made.
     */
    public void madeTradeMove(String player, int[] move) {
        List<Tile> moveSet = new ArrayList<>();
        for (int m : move) {
            moveSet.add(new Tile(m));
        }
        TradeMove tradeMove = new TradeMove(moveSet);
        for (SocketPlayer p : players) {
            if (player.equals(p.getName())) {
                p.setCurrentMove(tradeMove);
                break;
            }
        }
    }

    /**
     * This method is called when a PutMove is made with the player that made the move and the move itself as a list of
     * int arrays as parameters. It sets the corresponding SocketPlayer's currentMove to the move that was made.
     * @param player The player that made the move.
     * @param move The move that was made.
     */
    public void madePutMove(String player, List<int[]> move) { //move[i]: {x, y, id}
        Map<Location, Tile> moveSet = new HashMap<>();
        for (int[] m : move) {
            moveSet.put(new Location(m[0], m[1]), new Tile(m[2]));
        }
        PutMove putMove = new PutMove(moveSet);
        for (SocketPlayer p : players) {
            if (player.equals(p.getName())) {
                p.setCurrentMove(putMove);
                break;
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
        if (pool.isEmpty()) {
            for (SocketPlayer p : players) {
                if (p.getHand().isEmpty()) return true;
            }
        }
        return board.getPossibleMoves().isEmpty();
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
        for (SocketPlayer p : players) {
            if (player.equals(p.getName())) hand = p.getHand();
        }
        return hand;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<SocketPlayer> getPlayers() { return players;}

}
