package Model.Network;

/**
 * Created by Herjan on 20-1-2016.
 */

//import Model.Game.Game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Herjan on 20-1-2016.
 */
public class ClientHandler extends Thread {

    private Server server;
    private Socket sock;
    private BufferedReader in;
    private BufferedWriter out;
    public String clientName;
    private ClientHandler opponent;

    /**
     * Constructs a ClientHandler object
     * Initialises both Data streams.
     *@ requires server != null && sock != null;
     */
    public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
        this.server = serverArg;
        this.sock = sockArg;
        this.in = new BufferedReader(new InputStreamReader(sockArg.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(sockArg.getOutputStream()));
    }

    /**
     * Reads the name of a Client from the input stream and sends
     * a broadcast message to the Server to signal that the Client
     * is participating in the chat. Notice that this method should
     * be called immediately after the ClientHandler has been constructed.
     */
     public void announce() throws IOException {
     clientName = in.readLine();
     server.broadcast("[" + clientName + " has entered]", this);
     System.out.println(getName());}

    public void setClientName(String name) {
        this.clientName=name;
    }

    public String getClientName(){
        return clientName;
    }

    /**
     * This method can be used to send a message over the socket
     * connection to the Client. If the writing of a message fails,
     * the method concludes that the socket connection has been lost
     * and shutdown() is called.
     */
    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
        }

    }

    /**
     * This ClientHandler signs off from the Server and subsequently
     * sends a last broadcast to the Server to inform that the Client
     * is no longer participating in the chat.
     */
    /**private void shutdown() {
     *
     * }
     */
} // End of class ClientHandler
