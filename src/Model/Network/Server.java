package Model.Network;

/**
 * Created by Herjan on 20-1-2016.
 */
import Model.Game.Location;
import Model.Game.PutMove;
import Model.Game.ServerGame;
import Model.Game.Tile;
import View.ServerTUI;
import View.ServerView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by Herjan on 20-1-2016.
 */
public class Server extends Thread {

    private static final String USAGE = "usage: " + Server.class.getName()
            + " <port>";

    /** Starts a Server-application. */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(USAGE);
            //System.exit(0);
        }
        serverView = new ServerTUI();
        Scanner in = new Scanner(System.in);
        serverView.showMessage("Enter port number: ");
        String portInput = in.nextLine();
        port = Integer.parseInt(portInput);
        serverView.showMessage("Server now listening on port: " + port);
        Server server = new Server(port);
        serverView.showMessage("Server starting.");
        server.run();

    }

    public static String MSG_SEPARATOR = " ";
    public static String GAMESTART = "GAMESTART";
    public static String IDENTIFYOK = "IDENTIFYOK";

    private String checkChallenger;
    private PutMove realMove;
    private Map<Location, Tile> finalMove = new HashMap<>();

    private static int port;
    private static ServerView serverView;
    private List<ClientHandler> inactiveThreads;
    private List<ClientHandler> lobby;
    private List<ClientHandler> matchedPlayers;
    private List<ServerGame> activeGames;
    private List<ClientHandler> twoPlayerGame;
    private List<ClientHandler> threePlayerGame;
    private List<ClientHandler> fourPlayerGame;

    /**@param port the port that the server listens to.*/
    public Server(int port) {

        this.inactiveThreads = new ArrayList<>();
        this.lobby = new ArrayList<>();
        this.matchedPlayers = new ArrayList<>();
        this.activeGames = new ArrayList<>();
        this.twoPlayerGame = new ArrayList<>();
        this.threePlayerGame = new ArrayList<>();
        this.fourPlayerGame = new ArrayList<>();
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
            ServerSocket sSocket = new ServerSocket(port);
            while (true) {
                Socket socket = sSocket.accept();
                ClientHandler handler = new ClientHandler(this, socket);
                serverView.showMessage("Client no. " + (++i) + " connected.");
                handler.start();
                addInactiveHandler(handler);
            }
        } catch (IOException e) {
        }
    }

    List<String> serverFeatures = new ArrayList<>(); //TODO: put serverFeatures in list

    /**@param c the clientHandler which has to be used in the method since it has sent the command.*/
    public void identify(ClientHandler c) {
        inactiveThreads.remove(c);
        lobby.add(c);
        serverFeatures.add("LOBBY");
        serverFeatures.add("CHALLENGE");
        c.sendMessage(IDENTIFYOK + MSG_SEPARATOR + serverFeatures);
        serverFeatures.clear();
        c.sendMessage("Welcome to the lobby!");
    }

    // does not work properly yet. names takes the empty String instead of the one from the for-loop
    /**@param c the clientHandler which has to be used in the method since it has sent the command.*/
    public void lobby(ClientHandler c) {
        String names = "";
        for (ClientHandler aLobby : lobby) {
            names += " " + aLobby.getClientName();
        }
        c.sendMessage("LOBBYOK" + names);
    }

    /**@return the name of the client that has sent the challenge request.*/
    public String getCheckChallenger() {
        return checkChallenger;
    }

    /**@param challenger the name of the client that has sent the challenge request.*/
    public void setCheckChallenger(String challenger) {
        checkChallenger = challenger;
    }

    /**@param c the clientHandler which has to be used in the method since it has sent the command.
     * @param challenged the name of the client that has been challenged by another client.
     */
    public void challenge(ClientHandler c, String challenged) {
        for (ClientHandler aLobby : lobby) {
            if (aLobby.getClientName().equals(challenged)) {
                aLobby.sendMessage("CHALLENGEDBY " + c.getClientName());
                setCheckChallenger(c.getClientName());
            }
        }
    }

    /**@param c the clientHandler which has to be used in the method since it has sent the command.
     * @param challenger the name of the client that has sent the challenge request.
     */
    public void challengeDecline(ClientHandler c, String challenger) {
        for (ClientHandler aLobby : lobby) {
            if (aLobby.getClientName().equals(challenger)) {
                aLobby.sendMessage("CHALLENGE_DECLINEDBY " + c.getClientName());
            }
        }
    }

    /**@param c the clientHandler which has to be used in the method since it has sent the command.
     * @param challenger the name of the client that has sent the challenge request and to whom the challengeAccept command should be sent.
     */
    public void challengeAccept(ClientHandler c, String challenger) {
        System.out.println(getCheckChallenger());
        if (challenger.equals(getCheckChallenger())) {
            String[] playersList = new String[1];
            playersList[0] = challenger;
            ServerGame newServerGame = new ServerGame(playersList, challenger);
            c.sendMessage(GAMESTART + MSG_SEPARATOR + c.getClientName() + MSG_SEPARATOR + challenger);
            for (int i = 0; i < lobby.size(); i++) {
                if (lobby.get(i).getClientName().equals(challenger)) {
                    c.setGame(newServerGame);
                    serverView.showBoard(newServerGame.getBoard());
                    lobby.get(i).setGame(newServerGame);
                    lobby.get(i).sendMessage(GAMESTART + MSG_SEPARATOR + c.getClientName() + MSG_SEPARATOR + challenger);
                    lobby.remove(i);
                }
            }
            this.activeGames.add(newServerGame);
            lobby.remove(c);
        } else {
            c.sendMessage("This player hasn't challenged you");
        }
    }

    /**@param c the clientHandler which has to be used in the method since it has sent the command.
     * @param message the command that is sent by the clientHandler c.
     */
    public void queue(ClientHandler c, String message) {
        String[] numbers = message.split(",");
        for (int i = 0; i < numbers.length; i++) {
            int nr = Integer.parseInt(numbers[i]);
            if (nr == 2) {
                twoPlayerGame.add(c);
                if (twoPlayerGame.size() == 2) {
                    String [] playersList = new String[1];
                    playersList[0] = twoPlayerGame.get(0).getClientName();
                    ServerGame newGame = new ServerGame(playersList, twoPlayerGame.get(0).getClientName());
                    serverView.showBoard(newGame.getBoard());
                    c.setGame(newGame);
                    twoPlayerGame.get(0).setGame(newGame);
                    c.sendMessage(GAMESTART + MSG_SEPARATOR + c.getClientName() + MSG_SEPARATOR + twoPlayerGame.get(0).getClientName());
                    twoPlayerGame.get(0).sendMessage(GAMESTART + MSG_SEPARATOR + c.getClientName() + MSG_SEPARATOR + twoPlayerGame.get(0).getClientName());
                    this.activeGames.add(newGame);
                    lobby.remove(c);
                    lobby.remove(twoPlayerGame.get(0));
                }
            }
            else {
                if (nr == 3) {
                    threePlayerGame.add(c);
                    if (threePlayerGame.size() == 3) {
                        String[] playerList = new String[2];
                        playerList[0] = threePlayerGame.get(0).getClientName();
                        playerList[1] = threePlayerGame.get(1).getClientName();
                        ServerGame newGame = new ServerGame(playerList, threePlayerGame.get(0).getClientName());
                        for (ClientHandler aThreePlayerGame : threePlayerGame) {
                            c.setGame(newGame);
                            aThreePlayerGame.setGame(newGame);
                            aThreePlayerGame.sendMessage(GAMESTART + MSG_SEPARATOR + c.getClientName() + MSG_SEPARATOR + threePlayerGame.get(0) + threePlayerGame.get(2));
                            lobby.remove(threePlayerGame.get(i));
                        }
                        this.activeGames.add(newGame);
                    }
                } else {
                    if (nr == 4) {
                        fourPlayerGame.add(c);
                        if(fourPlayerGame.size() == 4) {
                            String[] playerList = new String[3];
                            playerList[0] = fourPlayerGame.get(0).getClientName();
                            playerList[1] = fourPlayerGame.get(1).getClientName();
                            playerList[2] = fourPlayerGame.get(2).getClientName();
                            ServerGame newGame = new ServerGame(playerList, fourPlayerGame.get(0).getClientName());
                            for (ClientHandler aFourPlayerGame : fourPlayerGame) {
                                c.setGame(newGame);
                                aFourPlayerGame.setGame(newGame);
                                aFourPlayerGame.sendMessage(GAMESTART + MSG_SEPARATOR + c.getClientName() + MSG_SEPARATOR + fourPlayerGame.get(0) + fourPlayerGame.get(2));
                                lobby.remove(fourPlayerGame.get(i));
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

    /**@param c the clientHandler which has to be used in the method since it has sent the command.*/
    public void quit(ClientHandler c) {
        //TODO: remove clientHandler from the game
    }

    /**@param c the clientHandler which has to be used in the method since it has sent the command.
     * @param blocks an array of the command that was sent by the clientHandler c split by a msgSeparator.
     */
    public void movePut(ClientHandler c, String[] blocks) {
        if (c.getClientName().equals(c.getGame().getCurrentPlayer().getName())) {
            List<int[]> putMove = new ArrayList<>();
            for (int i = 1; i < blocks.length; i++) {
                String move = blocks[i];
                String[] tileLoc = move.split("@");

                String locTile = tileLoc[1];
                String[] coords = locTile.split(",");

                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int id = Integer.parseInt(tileLoc[0]);
                int[] m = {x, y, id};

                // debug
                System.out.println("move:\t" + Arrays.toString(m));

                putMove.add(m);
            }
            c.getGame().madePutMove(c.getClientName(), putMove);
        }
        serverView.showBoard(c.getGame().getBoard());
    }

    /**@param c the clientHandler which has to be used in the method since it has sent the command.
     * @param tiles an array of the command that was sent by the clientHandler c split by a msgSeparator.
     */
    public void moveTrade(ClientHandler c, String[] tiles) {
        if (c.getClientName().equals((c.getGame().getCurrentPlayer().getName()))) {
            int[] tradeMove = new int[tiles.length - 1];
            for (int i = 1; i < tiles.length; i++) {
                int oldTile = Integer.parseInt(tiles[i]);

                // debug
                System.out.println("move:\t" + oldTile);

                tradeMove[i - 1] = oldTile;
            }
            c.getGame().madeTradeMove(c.getClientName(), tradeMove);
        }
        serverView.showBoard(c.getGame().getBoard());

//        c.sendMessage("MOVEOK_TRADE" + MSG_SEPARATOR + amount);
//        c.sendMessage("DRAWTILE" + MSG_SEPARATOR + newTiles);
    }

    /**@param msg the message that was sent by the clientHandler c.
     * @param c the clientHandler which has to be used in the method since it has sent the command.*/
    public void broadcast(String msg, ClientHandler c) {
        String[] splitArray = msg.split(MSG_SEPARATOR);
        serverView.showMessage("New message from " + c.getClientName() + ": " + msg);
        String challenger;
        switch (splitArray[0]) {
            case "IDENTIFY":
                c.setClientName(splitArray[1]);
                identify(c);
                break;
            case "LOBBY":
                lobby(c);
                break;
            case "CHALLENGE":
                String challenged = splitArray[1];
                challenge(c, challenged);
                break;
            case "CHALLENGE_DECLINE":
                challenger = splitArray[1];
                challengeDecline(c, challenger);
                break;
            case "CHALLENGE_ACCEPT":
                challenger = splitArray[1];
                challengeAccept(c, challenger);
                break;
            case "QUEUE":
                String message = splitArray[1];
                queue(c, message);
                break;
            case "QUIT":
                quit(c);
                break;
            case "MOVE_PUT":
                movePut(c, splitArray);
                break;
            case "MOVE_TRADE":
                moveTrade(c, splitArray);
                break;
        }
    }

    /**
     * Add a ClientHandler to the collection of ClientHandlers.
     *
     * @param handler ClientHandler that will be added.
     */
    public void addInactiveHandler(ClientHandler handler) {
        inactiveThreads.add(handler);
    }

} //End of class Server