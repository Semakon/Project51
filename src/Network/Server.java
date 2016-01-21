package Network;

/**
 * Created by Herjan on 20-1-2016.
 */
import java.util.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Herjan on 20-1-2016.
 */
public class Server extends Thread {

    private static final String USAGE = "usage: " + Server.class.getName()
            + " <port>";

    /** Start een Server-applicatie op. */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(USAGE);
            //System.exit(0);
        }
        Scanner in = new Scanner(System.in);
        System.out.println("Enter portnumber: ");
        String portInput = in.nextLine();
        port = Integer.parseInt(portInput);
        System.out.println("De server luistert nu naar poort: " + port);
        Server server = new Server(port);
        System.out.println("Server starting.");
        server.run();

    }

    private static int port;

    public Server(int port) {
        //this.port = port; //original: this.start();
        this.start();
    }

    /**
     * Listens to a port of this Server if there are any Clients that would like
     * to connect. For every new socket connection a new ClientHandler thread is
     * started that takes care of the further communication with the Client.
     */
    public void run() {
        int i = 0;
        try {
            ServerSocket ssocket = new ServerSocket(this.port);
            while (true) {
                Socket socket = ssocket.accept();
                ClientHandler handler = new ClientHandler(this, socket);
                System.out.println("[Client no. " + (++i) + "]" + "connected.");
                handler.start();
                //addInactiveHandler(handler);

            }
        } catch (IOException e) {
        }
    }
} //End of class Server