package Model.Game.Enumerations;

/**
 * Created by Martijn on 13-1-2016.
 */
public enum Color {

    Green, Yellow, Red, Purple, Orange, Blue;

    public int getId() {
        int id = -1;
        switch (this) {
            case Green: id = 0;
                break;
            case Yellow: id = 1;
                break;
            case Red: id = 2;
                break;
            case Blue: id = 3;
                break;
            case Purple: id = 4;
                break;
            case Orange: id = 5;
                break;
        }
        return id;
    }

    public String toString() {
        String color = "XX";
        switch (this) {
            case Green: color = "Gr";
                break;
            case Yellow: color = "Ye";
                break;
            case Orange: color = "Or";
                break;
            case Blue: color = "Bl";
                break;
            case Red: color = "Re";
                break;
            case Purple: color = "Pu";
        }
        return color;
    }

}
