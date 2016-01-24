package Model.Game;

import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Player.Player;
import Model.Player.SocketPlayer;

/**
 * Created by Martijn on 11-1-16.
 */
public class Game {

    private Board board;
    private Player[] players;
    private int currentPlayer;

    public Game(String thisPlayer, String[] players, String firstMove) {
        board = new Board();
        this.players = new Player[players.length];
        if (thisPlayer == null) {
            //Game on Server.
            for (int i = 0; i < players.length; i++) {
                try {
                    Player p = new SocketPlayer(players[i], board.getPool().takeTiles(6));
                    this.players[i] = p;
                } catch (InsufficientTilesInPoolException e) {
                    //should not occur
                    e.printStackTrace();
                }
            }
        } else {
            //Game on Client
            //TODO: implement
        }
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].getName().equals(firstMove)) {
                currentPlayer = i;
                break;
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

}
