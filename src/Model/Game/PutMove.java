package Model.Game;

import Model.Game.Enumerations.Identity;
import Model.Game.Enumerations.Positioning;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.Map;

/**
 * Created by Martijn on 13-1-2016.
 */
public class PutMove extends Move {

    private Identity identity = Identity.unspecified;
    private Positioning positioning = Positioning.unspecified;
    private Map<Location, Tile> move;

    /**
     * Creates a new instance of PutMove(). (to be continued)
     * @param move Move input by Player.
     */
    public PutMove(Map<Location, Tile> move) {
        this.move = move;
    }

    public Map<Location, Tile> getMove() {
        return move;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setPositioning(Positioning positioning) {
        this.positioning = positioning;
    }

    public Positioning getPositioning() {
        return positioning;
    }

}
