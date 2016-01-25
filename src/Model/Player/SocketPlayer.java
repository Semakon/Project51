package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * Created by Martijn on 22-1-2016.
 */
public class SocketPlayer extends Player {

    /**
     * Creates a new instance of Player with a name and a hand, that contains exactly Configuration.HAND Tiles.
     *
     * @param name The player's name.
     * @param hand The player's Tiles.
     */
    public SocketPlayer(String name, List<Tile> hand) {
        super(name, hand);
    }

    @Override
    public PutMove determinePutMove(Board board) {
        //Get move from server.
        return null;
    }

    @Override
    public TradeMove determineTradeMove(Board board) {
        //Get move from server.
        return null;
    }

    @Override
    public Move determineMove(Board board) throws InvalidMoveException {
        //Get choice from server.
        return null;
    }
}
