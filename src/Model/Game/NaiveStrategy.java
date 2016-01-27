package Model.Game;

import java.util.List;

/**
 * Created by Martijn on 27-1-2016.
 */
public class NaiveStrategy implements Strategy {

    private String name;

    public NaiveStrategy() {
        name = "Naive-Computer";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Move determineMove(Board board, List<Tile> hand) {
        Move move;

        if (board.getPossibleMoves().isEmpty()) {
            move = determineTradeMove(board, hand);
        } else {
            move = determinePutMove(board, hand);
        }
        return move;
    }

    @Override
    public TradeMove determineTradeMove(Board board, List<Tile> hand) {
        return null;
    }


    @Override
    public PutMove determinePutMove(Board board, List<Tile> hand) {
        return null;
    }
}
