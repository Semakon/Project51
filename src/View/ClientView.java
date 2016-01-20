package View;

import Model.Game.Move;

import java.net.InetAddress;

/**
 * Created by Martijn on 20-1-2016.
 */
public interface ClientView extends View {

    void run();

    Move makeMove();
    int playerType();
    void connect(String name, InetAddress host, int port);

}
