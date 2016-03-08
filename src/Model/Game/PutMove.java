package Model.Game;

import Model.Game.Enumerations.Identity;
import Model.Game.Enumerations.Positioning;
import Model.Game.Exceptions.InvalidMoveException;

import java.util.Map;

/**
 * This class is created when a move is made on the field (a PutMove). A PutMove is a map of Locations to Tiles with
 * fields that hold information about the PutMove and methods to get or modify that information.
 *
 * Created by Martijn on 13-1-2016.
 */
public class PutMove extends Move {

    /**
     * This PutMove's identity.
     */
    private Identity identity = Identity.unspecified;

    /**
     * This PutMove's positioning.
     */
    private Positioning positioning = Positioning.unspecified;

    /**
     * The actual map with the Locations and the corresponding Tiles.
     */
    private Map<Location, Tile> move;

    /**
     * Creates a new instance of PutMove with a map of Locations and a Tiles.
     * @param move Move input by Player.
     */
    public PutMove(Map<Location, Tile> move) {
        this.move = move;
    }

    /**
     * Returns the actual move, a map with the Locations and Tiles.
     * @return The map with the Locations and Tiles.
     */
    public Map<Location, Tile> getMove() {
        return move;
    }

    /**
     * Changes this PutMove's Identity to the parameter identity.
     * @param identity The new identity.
     */
    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    /**
     * Returns this PutMove's Identity.
     * @return This PutMove's Identity.
     */
    public Identity getIdentity() {
        return identity;
    }

    /**
     * Changes this PutMove's Positioning to the parameter positioning.
     * @param positioning The new Positioning.
     */
    public void setPositioning(Positioning positioning) {
        this.positioning = positioning;
    }

    /**
     * Returns this PutMove's Positioning.
     * @return This PutMove's Positioning.
     */
    public Positioning getPositioning() {
        return positioning;
    }

}
