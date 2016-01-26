package Model.Network;

/**
 * Created by Herjan on 20-1-2016.
 */
import Model.Game.Exceptions.InsufficientTilesInPoolException;
import Model.Game.Exceptions.InvalidMoveException;
import Model.Game.Game;
import Model.Game.Location;
import Model.Game.PutMove;
import Model.Game.Tile;

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
            ServerSocket ssocket = new ServerSocket(port);
            while (true) {
                Socket socket = ssocket.accept();
                ClientHandler handler = new ClientHandler(this, socket);
                print("[Client no. " + (++i) + "]" + "connected.");
                handler.start();
                addInactiveHandler(handler);
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
        serverFeatures.add("LOBBY");
        serverFeatures.add("CHALLENGE");
        c.sendMessage(IDENTIFYOK + msgSeperator + serverFeatures);
        serverFeatures.clear();
        c.sendMessage("Welcome to the lobby!");
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
        String [] playersList = new String[1];
        playersList[0] = uitdager;
        Game newGame = new Game(c.getClientName(), playersList, uitdager);
        c.sendMessage(GAMESTART + msgSeperator + c.getClientName() + msgSeperator + uitdager);
        for (int i = 0; i < lobby.size(); i++) {
            if(lobby.get(i).getClientName().equals(uitdager)) {
                c.setGame(newGame);
                lobby.get(i).setGame(newGame);
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
                    String [] playersList = new String[1];
                    playersList[0] = twoPlayerGame.get(0).getClientName();
                    Game newGame = new Game(c.getClientName(), playersList, twoPlayerGame.get(0).getClientName());
                    c.setGame(newGame);
                    twoPlayerGame.get(0).setGame(newGame);
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
                        String [] playerList = new String[2];
                        playerList[0] = threePlayerGame.get(0).getClientName();
                        playerList[1] = threePlayerGame.get(1).getClientName();
                        Game newGame = new Game(c.getClientName(), playerList, threePlayerGame.get(0).getClientName());
                        for (int j = 0; j < threePlayerGame.size(); j++) {
                            c.setGame(newGame);
                            threePlayerGame.get(j).setGame(newGame);
                            threePlayerGame.get(j).sendMessage(GAMESTART + msgSeperator + c.getClientName() + msgSeperator + threePlayerGame.get(0) + threePlayerGame.get(2));
                            lobby.remove(threePlayerGame.get(i));
                        }
                        this.activeGames.add(newGame);
                    }
                } else {
                    if (nr == 4) {
                        fourPlayerGame.add(c);
                        if(fourPlayerGame.size() == 4) {
                            String [] playerList = new String[3];
                            playerList[0] = fourPlayerGame.get(0).getClientName();
                            playerList[1] = fourPlayerGame.get(1).getClientName();
                            playerList[2] = fourPlayerGame.get(2).getClientName();
                            Game newGame = new Game(c.getClientName(), playerList, fourPlayerGame.get(0).getClientName());
                            for (int k = 0; k < fourPlayerGame.size(); k++) {
                                c.setGame(newGame);
                                fourPlayerGame.get(k).setGame(newGame);
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

    private PutMove realMove;

    public void movePut(ClientHandler c, String [] blocks) {
        for(int i = 1; i < blocks.length; i++) {
            String move = blocks[i];
            String [] tileLoc = move.split("@");
            String locTile = tileLoc[1];
            String [] coords = locTile.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            System.out.println("int x: " + x);
            System.out.println("int y: " + y);

            Location location = new Location(x,y);

            System.out.println("Client name: " + c.getClientName());
            System.out.println("Current player: " + c.getGame().getCurrentPlayer().getName());
            System.out.println("Open locations: " + c.getGame().getBoard().getOpenLocations());
            System.out.println("Open locations size: " + c.getGame().getBoard().getOpenLocations().size());

            for(int k = 0; k < c.getGame().getBoard().getOpenLocations().size(); k++) {
                if(c.getGame().getBoard().getOpenLocations().get(i).isEqualTo(location)) {
                    for(int j = 0; j < c.getGame().getCurrentPlayer().getHand().size(); j++) {
                        Tile tile = c.getGame().getCurrentPlayer().getHand().get(i);
                        if(tile.equals(Integer.parseInt(tileLoc[0]))) {
                            print("Alleen de move nog leggen");
                            realMove.getMove().put(location, tile);
                            try {
                                c.getGame().getBoard().makePutMove(realMove);
                            } catch (InvalidMoveException e) {
                                e.printStackTrace();
                            }
                            c.getGame().getCurrentPlayer().getHand().remove(tile);
                            print("einde van de methode, alles ging goed");

                            /**String resultMove = "MOVEOK_PUT" + tiles;
                            for(int i = 0; i < aantalSpelers.length; i++) {
                                aantalSpelers.get(i).sendMessage(resultMove);
                            }
                            if (c.getGame().hasWinner()) {
                                c.getGame().printResult();
                                endGameWinner(c);
                            }
                        } else {
                            invalidUserTurn(c);
                        }*/
                        }
                    }
                }
            }
        }
    }

    private List<Tile> oldTiles;
    private List<Tile> newTiles;
    int amount;

    public void moveTrade(ClientHandler c, String [] tiles) {
        for(int i = 1; i < tiles.length; i++) {
            String blocks = tiles[i];
            int tile = Integer.parseInt(blocks);
            c.getGame().getCurrentPlayer().getHand().remove(tile);
            amount = tiles.length - 1;
            try {
                newTiles = c.getGame().getBoard().getPool().takeTiles(amount);
                System.out.println("NewTiles: " + newTiles);
                //oldTiles.add(tile); //oude tiles moeten omgezet worden van een String/int naar een Tile
                //c.getGame().getBoard().getPool().tradeTiles(oldTiles);
            } catch (InsufficientTilesInPoolException e) {
                e.printStackTrace();
            }
            for(int k = 0; k < newTiles.size(); k++) {
                c.getGame().getCurrentPlayer().getHand().add(newTiles.get(k));
            }
        }
        c.sendMessage("MOVEOK_TRADE" + msgSeperator + amount);
        c.sendMessage("DRAWTILE" + msgSeperator + newTiles);
    }

    public void broadcast(String msg, ClientHandler c) {
        String[] splitArray = msg.split(msgSeperator);
        print("New message from " + c.getClientName() + ": " + msg);
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
                                    else {
                                        if(splitArray[0].equals("MOVE_PUT")) {
                                            movePut(c, splitArray);
                                        }
                                        else {
                                            if(splitArray[0].equals("MOVE_TRADE")) {
                                                moveTrade(c, splitArray);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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