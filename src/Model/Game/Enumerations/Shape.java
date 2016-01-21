package Model.Game.Enumerations;

/**
 * Created by Martijn on 13-1-2016.
 */
public enum Shape {

    Cross, Plus, Square, Star, Circle, Diamond;

    public String toString() {
        String shape = "XX";
        switch (this) {
            case Cross: shape = "Cr";
                break;
            case Plus: shape = "Pl";
                break;
            case Square: shape = "Sq";
                break;
            case Star: shape = "St";
                break;
            case Circle: shape = "Ci";
                break;
            case Diamond: shape = "Di";
        }
        return shape;
    }

}
