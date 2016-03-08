package Model.Player;

import Model.Game.*;
import Model.Game.Exceptions.InvalidAmountRuntimeException;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * Created by Martijn on 11-1-2016.
 *
 * This abstract class generalizes all other Player classes.
 */
public abstract class Player {

    /**
     * This player's name.
     */
    private String name;

    /**
     * This player's hand.
     */
    private List<Tile> hand;

    /**
     * Creates a new instance of Player with a name and a hand, that contains exactly Configuration.HAND Tiles.
     * @param name The player's name.
     * @param hand The player's Tiles.
     */
    public Player(String name, List<Tile> hand) {
        this.name = name;
        if (hand.size() != Configuration.HAND) {
            throw new InvalidAmountRuntimeException("The player's hand must contain exactly " + Configuration.HAND + " Tiles.");
        }
        this.hand = hand;
    }

    /**
     * Returns the player's name.
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's hand.
     * @return The player's hand.
     */
    public List<Tile> getHand() {
        return hand;
    }

    /**
     * Determines the next move.
     * @param board the current board.
     * @return the player's choice.
     */
    public abstract Move determineMove(Board board) throws InvalidMoveException;

    /**
     * Makes a move on the board.
     * @param board the current board.
     */
    public Move makeMove(Board board) {
        Move move;
        try {

            // Determine the move
            move = determineMove(board);

            // validate the move and send it to the server
            validateMove(move, board);

        } catch (InvalidMoveException e) {

            // if the move is invalid
            move = null;
            e.getMessage(); //TODO: display message to View and try again.
        }
        return move;
    }

    /**
     * Checks whether a move is valid. If a move is invalid, throws an InvalidMoveException.
     * @param move The Move that is to be validated.
     * @param board The Board that the Move is to be validated on.
     * @throws InvalidMoveException When the move is invalid.
     */
    private void validateMove(Move move, Board board) throws InvalidMoveException {
        // check if the move is a PutMove or TradeMove
        if (move instanceof PutMove) {
            if (new PutMoveValidator(board, (PutMove)move).validMove()) {
                //TODO: Send move to server.

            } else throw new InvalidMoveException();

        } else if (move instanceof TradeMove) {
            if (new TradeMoveValidator((TradeMove)move).correctHand(hand)) {
                //TODO: Send move to server.

            } else throw new InvalidMoveException();
        }
    }

}
