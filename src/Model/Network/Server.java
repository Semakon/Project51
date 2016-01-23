package Model.Network;

/**
 * Created by Herjan on 20-1-2016.
 */
import Model.Game.Game;
import Model.Game.Tile;
import Model.Player.HumanPlayer;

import java.util.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Model.Network.*;

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

    String msgSeperator = " ";
    String startGame = "startGame";
    String IDENTIFYOK = "IDENTIFYOK";

    private static int port;
    private List<ClientHandler> inactiveThreads;
    private List<ClientHandler> waitingThreads;
    private List<ClientHandler> matchedPlayers;
    private List<Game> activeGames;

    public Server(int port) {
        this.inactiveThreads = new ArrayList<ClientHandler>();
        this.waitingThreads = new ArrayList<ClientHandler>();
        this.matchedPlayers = new ArrayList<ClientHandler>();
        this.activeGames = new ArrayList<Game>();
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
                addInactiveHandler(handler);
                System.out.println(inactiveThreads);
            }
        } catch (IOException e) {
        }
    }

    public void print(String msg) {
        System.out.println(msg);
    }

    public void identify(ClientHandler c) {
        inactiveThreads.remove(c);
        waitingThreads.add(c);
        c.sendMessage("Welcome!");
        c.sendMessage(IDENTIFYOK);
        if (waitingThreads.size() == 2) {
            this.matchedPlayers.add(waitingThreads.get(0));
            this.matchedPlayers.add(waitingThreads.get(1));
            //TODO: Game newGame = new Game(?);
            //this.activeGames.add(newGame);
            for (ClientHandler c1 : waitingThreads) {

                print("Sending to " + c1.clientName + " to start the game!");
                c1.sendMessage(startGame + msgSeperator
                        + waitingThreads.get(0).getClientName()
                        + msgSeperator
                        + waitingThreads.get(1).getClientName());
            }
            this.waitingThreads = new ArrayList<ClientHandler>();
        } else {
            //waitingThreads.remove(c);
            //inactiveThreads.add(c);
            //invalidUserName error
        }
    }

    public void broadcast(String msg, ClientHandler c) {
        String[] splitArray = msg.split(msgSeperator);
        System.out.println("New message from " + c.getClientName() + ": " + msg);
        if (splitArray[0].equals("IDENTIFY")) {
            c.setClientName(splitArray[1]);
            System.out.println("JAHOOR");
            identify(c);/**
        } else {
            if (splitArray[0].equals(ProtocolControl.getBoard)) {
                sendBoard(c);
            } else {
                if (splitArray[0].equals(ProtocolControl.playerTurn)) {
                    turn(c);
                } else {
                    if (splitArray[0].equals(ProtocolControl.doMove)) {
                        makeMove(c, msg);
                    }
                }
            }*/
        }
    }

    /**
     * Add a ClientHandler to the collection of ClientHandlers.
     *
     * @param handler
     *            ClientHandler that will be added
     */
    public void addInactiveHandler(ClientHandler handler) {
        inactiveThreads.add(handler);
    }

} //End of class Server