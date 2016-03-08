package Model.Game;

import Model.Player.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martijn on 30-1-2016.
 *
 * This class is the game that runs on the client. The client's player has his/her own field and the other players
 * are contained in a list of SocketPlayers.
 */
public class ClientGame {

    /**
     * The Board associated with this game.
     */
    private Board board;

    /**
     * List of other players.
     */
    private List<SocketPlayer> otherPlayers;

    /**
     * The player who plays on this client.
     */
    private Player thisPlayer;

    /**
     * The player whose turn it currently is.
     */
    private Player currentPlayer;

    /**
     * Constructs a new ClientGame with initial values for the fields.
     * @param strategy The Strategy the computer player will be using, if there is one.
     * @param thisPlayer The name of the human player, if there is one.
     * @param thisHand The hand of the player that plays on this client.
     * @param otherPlayers The names of the other players.
     */
    public ClientGame(Strategy strategy, String thisPlayer, List<Tile> thisHand, String[] otherPlayers) {

        // initialize board and this.otherPlayers
        board = new Board();
        this.otherPlayers = new ArrayList<>();
        for (String player : otherPlayers) this.otherPlayers.add(new SocketPlayer(player, null));

        // initialize this.thisPlayer: If the strategy is null, create a human player, otherwise a computer player
        this.thisPlayer = strategy == null ? new HumanPlayer(thisPlayer, thisHand) :
                new ComputerPlayer(strategy, thisHand);
    }

    /**
     * Returns this game's Board.
     * @return This game's Board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the hand of the player.
     * @return Hand of player.
     */
    public List<Tile> getHand() {
        return thisPlayer.getHand();
    }

    /**
     * Returns the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player to the player with name <code>name</code>.
     * @param name Name of new current player.
     */
    public void setCurrentPlayer(String name) {
        // if the current player is the client's player
        if (thisPlayer.getName().equals(name)) currentPlayer = thisPlayer;

        else {
            // set the current player from otherPlayers
            for (SocketPlayer p : otherPlayers) {
                if (p.getName().equals(name)) {
                    currentPlayer = p;
                    return;
                }
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

        // This client's player's move is approved
        if (thisPlayer.getName().equals(player)) return;

        // Construct TradeMove from parameter 'move'
        List<Tile> moveSet = new ArrayList<>();
        for (int m : move) moveSet.add(new Tile(m));
        TradeMove tradeMove = new TradeMove(moveSet);

        // find player that made the move and set its current move to the newly constructed tradeMove
        for (SocketPlayer p : otherPlayers) {
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

        // This client's player's move is approved
        if (thisPlayer.getName().equals(player)) return;

        // Construct PutMove from parameter 'move'
        Map<Location, Tile> moveSet = new HashMap<>();
        for (int[] m : move) moveSet.put(new Location(m[0], m[1]), new Tile(m[2]));
        PutMove putMove = new PutMove(moveSet);

        // find player that made the move and set its current move to the newly constructed putMove
        for (SocketPlayer p : otherPlayers) {
            if (player.equals(p.getName())) {
                p.setCurrentMove(putMove);
                break;
            }
        }
    }

}
