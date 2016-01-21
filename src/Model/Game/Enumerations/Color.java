package Model.Game.Enumerations;

/**
 * Created by Martijn on 13-1-2016.
 */
public enum Color {

    Green, Yellow, Red, Purple, Orange, Blue;

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
