package View;

import Model.Game.Board;
import Model.Network.Server;

import java.util.Scanner;

/**
 * Created by Martijn on 27-1-2016.
 */
public class ServerTUI implements ServerView {
    @Override
    public void start() {}


    @Override
    public void showBoard(Board board) {
        System.out.println(board.toString());
    }

    @Override
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public void showError(String error) {
        System.err.println(error);
    }
}
