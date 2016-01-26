package Model.Game.Enumerations;

/**
 * Created by Martijn on 13-1-2016.
 */
public enum Shape {

    Cross, Plus, Square, Star, Circle, Diamond;

    public int getId() {
        int id = -1;
        switch (this) {
            case Cross: id = 0;
                break;
            case Plus: id = 1;
                break;
            case Square: id = 2;
                break;
            case Star: id = 3;
                break;
            case Circle: id = 4;
                break;
            case Diamond: id = 5;
                break;
        }
        return id;
    }

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
