package Model.Player;

import Model.Game.Board;
import Model.Game.Move;
import View.View;

/**
 * Created by Martijn on 20-1-2016.
 */
public class HumanPlayer extends Player {

    /**
     * Creates a new instance of Player with a name.
     * @param name the player's name.
     * @param UI the UI.
     */
    public HumanPlayer(String name, View UI) {
        super(name, UI);
    }

    @Override
    public Move determineMove(Board board) {
        //TODO: implement
        return null;
    }

}
