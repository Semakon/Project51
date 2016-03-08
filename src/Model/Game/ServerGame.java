package Model.Game;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Player.Player;
import Model.Player.SocketPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the game that runs on the server. It contains a list of SocketPlayers that act as substitutes for real
 * players. This class keeps track of everything that happens in the game:
 * The board, the pool, the players and the score.
 *
 * Created by Martijn on 11-1-16.
 */
public class ServerGame {

    /**
     * The Board associated with this game.
     */
    private Board board;

    /**
     * The players that participate in this game.
     */
    private List<SocketPlayer> players;

    /**
     * The player whose turn it currently is.
     */
    private SocketPlayer currentPlayer;

    /**
     * The Pool associated with this game.
     */
    private Pool pool;

    /**
     * Creates an instance of ServerGame. Assigns initial values to the fields.
     * @param players Array of names of players.
     * @param firstMove Name of player with the first move.
     */
    public ServerGame(String[] players, String firstMove) {

        // initialize fields
        board = new Board();
        pool = new Pool();
        this.players = new ArrayList<>();

        try {
            // Construct SocketPlayers with the players' names (their hand is taken from the pool)
            for (String player : players) {
                this.players.add(new SocketPlayer(player, pool.takeTiles(Configuration.HAND)));
            }
            // Set the current player
            for (SocketPlayer p : this.players) {
                if (firstMove.equals(p.getName())) currentPlayer = p;
            }
        } catch (InsufficientTilesInPoolException e) {
            // should not occur
            e.printStackTrace();
            System.exit(0);
        }

        play();
    }

    /**
     * Returns this game's Pool.
     * @return This game's Pool.
     */
    public Pool getPool() {
        return pool;
    }

    /**
     * Returns this game's Board.
     * @return This game's Board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the hand of the player with name <code>player</code>.
     * @param name The player's name.
     * @return Hand of player with name <code>player</code>. Null if the player can't be found.
     */
    public List<Tile> getHand(String name) {
        // find the player with 'name' and return this hand
        for (SocketPlayer player : players) {
            if (name.equals(player.getName())) return player.getHand();
        }
        return null;
    }

    /**
     * Returns the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns a list of players.
     * @return A list of players.
     */
    public List<SocketPlayer> getPlayers() {
        return players;
    }

    /**
     * This methods controls the game until the game is over.
     */
    public void play() {
        boolean done = false;
        while (!done) {

            //TODO: wait on client input (move)

            currentPlayer.makeMove(board);

            //TODO: change current player

            if (gameOver()) {

                //TODO: implement further

                done = true;
            }

            //TODO: inform clients of new currentPlayer

        }
    }

    /**
     * This method is called when a TradeMove is made with the player that made the move and the move itself as an int
     * array as parameters. It sets the corresponding SocketPlayer's currentMove to the move that was made.
     * @param player The player that made the move.
     * @param move The move that was made.
     */
    public void madeTradeMove(String player, int[] move) {

        // Construct TradeMove from parameter 'move'
        List<Tile> moveSet = new ArrayList<>();
        for (int m : move) moveSet.add(new Tile(m));
        TradeMove tradeMove = new TradeMove(moveSet);

        // find player that made the move and set its current move to the newly constructed tradeMove
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
     * The move should be constructed as follows:
     *
     * move[i]: {X-coordinate, Y-coordinate, Tile's ID}
     *
     * @param player The player that made the move.
     * @param move The move that was made.
     */
    public void madePutMove(String player, List<int[]> move) {

        // Construct PutMove from parameter 'move'
        Map<Location, Tile> moveSet = new HashMap<>();
        for (int[] m : move) moveSet.put(new Location(m[0], m[1]), new Tile(m[2]));
        PutMove putMove = new PutMove(moveSet);

        // find player that made the move and set its current move to the newly constructed putMove
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
        // if the pool is empty and the currentPlayer's hand is empty return true
        if (pool.isEmpty()) {
            if (currentPlayer.getHand().isEmpty()) return true;
        }

        // check if there are any possible moves left on the board and return the result
        return board.getPossibleMoves().isEmpty();
    }

}
