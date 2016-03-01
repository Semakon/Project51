package Model.Game.Enumerations;

/**
 * Created by Martijn on 13-1-2016.
 *
 * The positioning of a PutMove is determined by the PutMove's position. Vertical obviously means all blocks are
 * vertical and horizontal obviously means all blocks are horizontal. The positioning is unspecified if the move exists
 * of only one block and it's invalid if none of the others apply.
 */
public enum Positioning {

    unspecified, invalid, vertical, horizontal

}
