package Model.Game;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Player.ComputerPlayer;
import Model.Player.HumanPlayer;
import Model.Player.Player;
import Model.Player.SocketPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martijn on 30-1-2016.
 */
public class ClientGame {

    private Board board;
    private List<Player> players;
    private Player currentPlayer;

    public ClientGame(Strategy strategy, String thisPlayer, String[] players) {
        board = new Board();
        this.players = new ArrayList<>();
        try {
            if (strategy == null) {
                this.players.add(new HumanPlayer(thisPlayer, board.getPool().takeTiles(Configuration.HAND)));
            } else {
                this.players.add(new ComputerPlayer(strategy, board.getPool().takeTiles(Configuration.HAND)));
            }
            for (String player : players) {
                this.players.add(new SocketPlayer(player, board.getPool().takeTiles(Configuration.HAND)));
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

    public Move madeMove(String player, Move move) {

        return null;
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

    /**
     * Sets the current player to the player with name <code>name</code>.
     * @param name Name of new current player.
     */
    public void setCurrentPlayer(String name) {
        Player currentPlayer = null;
        for (Player p : players) {
            if (name.equals(p.getName())) currentPlayer = p;
        }
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

}
