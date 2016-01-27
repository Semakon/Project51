package View;

import Model.Game.Board;
import Model.Game.Move;

import java.net.InetAddress;

/**
 * Created by Martijn on 27-1-2016.
 */
public class ClientTUI implements ClientView {
    @Override
    public void run() {

    }

    @Override
    public Move makeMove() {
        return null;
    }

    @Override
    public int playerType() {
        return 0;
    }

    @Override
    public void connect(String name, InetAddress host, int port) {

    }

    @Override
    public void start() {

    }

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
