package View;

import Model.Game.Board;

/**
 * Created by Martijn on 20-1-2016.
 */
public interface View {

    void start();

    void showBoard(Board board);
    void showMessage(String msg);
    void showError(String error);

}
