package Model.Network;

/**
 * Created by Herjan on 20-1-2016.
 */
import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Game.Game;

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

    String msgSeperator = " ";
    String GAMESTART = "GAMESTART";
    String IDENTIFYOK = "IDENTIFYOK";

    private static int port;
    private List<ClientHandler> inactiveThreads;
    private List<ClientHandler> lobby;
    private List<ClientHandler> matchedPlayers;
    private List<Game> activeGames;
    private List<ClientHandler> twoPlayerGame;
    private List<ClientHandler> threePlayerGame;
    private List<ClientHandler> fourPlayerGame;

    public Server(int port) {
        this.inactiveThreads = new ArrayList<ClientHandler>();
        this.lobby = new ArrayList<ClientHandler>();
        this.matchedPlayers = new ArrayList<ClientHandler>();
        this.activeGames = new ArrayList<Game>();
        this.twoPlayerGame = new ArrayList<ClientHandler>();
        this.threePlayerGame = new ArrayList<ClientHandler>();
        this.fourPlayerGame = new ArrayList<ClientHandler>();
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

    List<String> serverFeatures = new ArrayList<String>(); //TODO: serverFeatures in lijst stoppen

    public void identify(ClientHandler c) {
        inactiveThreads.remove(c);
        lobby.add(c);
        c.sendMessage(IDENTIFYOK + msgSeperator + serverFeatures);
        c.sendMessage("Welcome to the lobby!");

        //Dit moet in de methode die een game opstart, waarschijnlijk bij client_queue of client_challenge_accept
        /**if (lobby.size() == 2) {
            this.matchedPlayers.add(lobby.get(0));
            this.matchedPlayers.add(lobby.get(1));
            //TODO: Game newGame = new Game(?);
            //this.activeGames.add(newGame);
            for (ClientHandler c1 : lobby) {

                print("Sending to " + c1.clientName + " to start the game!");
                c1.sendMessage(GAMESTART + msgSeperator
                        + lobby.get(0).getClientName()
                        + msgSeperator
                        + lobby.get(1).getClientName());
            }
            this.lobby = new ArrayList<ClientHandler>();
        } else {
            //lobby.remove(c);
            //inactiveThreads.add(c);
            //invalidUserName error
        }*/
    }

    //werkt nog niet helemaal correct, names pakt nog de lege string ipv die uit de for-loop
    public void lobby(ClientHandler c) {
        String names = "";
        for(int i = 0; i < lobby.size(); i++) {
            names += " " + lobby.get(i).getClientName();
        }
        c.sendMessage("LOBBYOK" + names);
    }

    public void challenge(ClientHandler c, String uitgedaagde) {
        for (int i = 0; i < lobby.size(); i++) {
            if (lobby.get(i).getClientName().equals(uitgedaagde)) {
                lobby.get(i).sendMessage("CHALLENGEDBY " + c.getClientName());
            }
        }
    }

    public void challengeDecline(ClientHandler c, String uitdager) {
        for (int i = 0; i < lobby.size(); i++) {
            if (lobby.get(i).getClientName().equals(uitdager)) {
                lobby.get(i).sendMessage("CHALLENGE_DECLINEDBY " + c.getClientName());
            }
        }
    }

    public void challengeAccept(ClientHandler c, String uitdager) {
        //TODO: start a game between c and uitdager
        String [] playersList = new String[1];
        playersList[0] = uitdager;
        Game newGame = new Game(c.getClientName(), playersList, uitdager);
        c.sendMessage(GAMESTART + msgSeperator + c.getClientName() + msgSeperator + uitdager);
        for (int i = 0; i < lobby.size(); i++) {
            if(lobby.get(i).getClientName() == uitdager) {
                lobby.get(i).sendMessage(GAMESTART + msgSeperator + c.getClientName() + msgSeperator + uitdager);
                lobby.remove(i);
            }
        }
        this.activeGames.add(newGame);
        lobby.remove(c);
    }

    public void queue(ClientHandler c, String message) {
        String [] numbers = message.split(",");
        for (int i = 0; i < numbers.length; i++) {
            int nr = Integer.parseInt(numbers[i]);
            if (nr == 2) {
                twoPlayerGame.add(c);
                if (twoPlayerGame.size() == 2) {
                    //TODO: start a game
                    String [] playersList = new String[1];
                    playersList[0] = twoPlayerGame.get(0).getClientName();
                    Game newGame = new Game(c.getClientName(), playersList, twoPlayerGame.get(0).getClientName());
                    c.sendMessage(GAMESTART + msgSeperator + c.getClientName() + msgSeperator + twoPlayerGame.get(0).getClientName());
                    twoPlayerGame.get(0).sendMessage(GAMESTART + msgSeperator + c.getClientName() + msgSeperator + twoPlayerGame.get(0).getClientName());
                    this.activeGames.add(newGame);
                    lobby.remove(c);
                    lobby.remove(twoPlayerGame.get(0));
                }
            }
            else {
                if (nr == 3) {
                    threePlayerGame.add(c);
                    if (threePlayerGame.size() == 3) {
                        //TODO: start a game
                        String [] playerList = new String[2];
                        playerList[0] = threePlayerGame.get(0).getClientName();
                        playerList[1] = threePlayerGame.get(1).getClientName();
                        Game newGame = new Game(c.getClientName(), playerList, threePlayerGame.get(0).getClientName());
                        for (int j = 0; j < threePlayerGame.size(); j++) {
                            threePlayerGame.get(j).sendMessage(GAMESTART + msgSeperator + c.getClientName() + msgSeperator + threePlayerGame.get(0) + threePlayerGame.get(2));
                            lobby.remove(threePlayerGame.get(i));
                        }
                        this.activeGames.add(newGame);
                    }
                } else {
                    if (nr == 4) {
                        fourPlayerGame.add(c);
                        if(fourPlayerGame.size() == 4) {
                            //TODO: start a game
                            String [] playerList = new String[3];
                            playerList[0] = threePlayerGame.get(0).getClientName();
                            playerList[1] = threePlayerGame.get(1).getClientName();
                            playerList[2] = threePlayerGame.get(2).getClientName();
                            Game newGame = new Game(c.getClientName(), playerList, fourPlayerGame.get(0).getClientName());
                            for (int k = 0; k < fourPlayerGame.size(); k++) {
                                fourPlayerGame.get(k).sendMessage(GAMESTART + msgSeperator + c.getClientName() + msgSeperator + fourPlayerGame.get(0) + fourPlayerGame.get(2));
                                lobby.remove(threePlayerGame.get(i));
                            }
                            this.activeGames.add(newGame);
                        }
                    }
                    else {
                        c.sendMessage("WRONGNUMBER");
                    }
                }
            }
        }
    }

    public void quit(ClientHandler c) {
        //TODO: remove clienthandler from the game
    }

    public void broadcast(String msg, ClientHandler c) {
        String[] splitArray = msg.split(msgSeperator);
        System.out.println("New message from " + c.getClientName() + ": " + msg);
        if (splitArray[0].equals("IDENTIFY")) {
            c.setClientName(splitArray[1]);
            identify(c);
        } else {
                if (splitArray[0].equals("LOBBY")) {
                    lobby(c);}
            else {
                    if (splitArray[0].equals("CHALLENGE")) {
                        String uitgedaagde = splitArray[1];
                        challenge(c, uitgedaagde);
                    }
                    else {
                        if (splitArray[0].equals("CHALLENGE_DECLINE")) {
                            String uitdager = splitArray[1];
                            challengeDecline(c, uitdager);
                        }
                        else {
                            if(splitArray[0].equals("CHALLENGE_ACCEPT")) {
                                String uitdager = splitArray[1];
                                challengeAccept(c, uitdager);
                            }
                            else {
                                if(splitArray[0].equals("QUEUE")) {
                                    String message = splitArray[1];
                                    queue(c, message);
                                }
                                else {
                                    if(splitArray[0].equals("QUIT")) {
                                        quit(c);
                                    }
                                }
                            }
                        }
                    }

                }

            /**


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