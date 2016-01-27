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

    }

    @Override
    public void showMessage(String msg) {

    }

    @Override
    public void showError(String error) {

    }
}
