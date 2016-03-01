package Model.Game.Enumerations;

/**
 * Created by Martijn on 13-1-2016.
 *
 * The identity of a PutMove is represented by this class. A 'color' identity means all colors in the PutMove are the
 * same. A 'shape' identity means all shapes in a PutMove are the same. The identity is unspecified if the move exists
 * of only one block and can't therefore not be specified. The identity is invalid if none of the others apply.
 */
public enum Identity {

    unspecified, invalid, color, shape

}
