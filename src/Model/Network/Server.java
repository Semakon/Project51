package Model.Network;

/**
 * Created by Herjan on 20-1-2016.
 */
import Model.Game.Exceptions.InvalidMoveException;
import Model.Game.ServerGame;
import Model.Game.Location;
import Model.Game.PutMove;
import Model.Game.Tile;
import View.ServerTUI;
import View.ServerView;

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
        serverView = new ServerTUI();
        Scanner in = new Scanner(System.in);
        serverView.showMessage("Enter portnumber: ");
        String portInput = in.nextLine();
        port = Integer.parseInt(portInput);
        serverView.showMessage("De server luistert nu naar poort: " + port);
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
                serverView.showMessage("[Client no. " + (++i) + "]" + "connected.");
                handler.start();
                addInactiveHandler(handler);
            }
        } catch (IOException e) {
        }
    }

    List<String> serverFeatures = new ArrayList<String>(); //TODO: serverFeatures in lijst stoppen

    public void identify(ClientHandler c) {
        inactiveThreads.remove(c);
        lobby.add(c);
        serverFeatures.add("LOBBY");
        serverFeatures.add("CHALLENGE");
        c.sendMessage(IDENTIFYOK + MSG_SEPARATOR + serverFeatures);
        serverFeatures.clear();
        c.sendMessage("Welcome to the lobby!");
    }

    //werkt nog niet helemaal correct, names pakt nog de lege string ipv die uit de for-loop
    public void lobby(ClientHandler c) {
        String names = "";
        for (ClientHandler aLobby : lobby) {
            names += " " + aLobby.getClientName();
        }
        c.sendMessage("LOBBYOK" + names);
    }

    public String getCheckChallenger() {
        return checkChallenger;
    }

    public void setCheckChallenger(String challenger) {
        checkChallenger = challenger;
    }

    public void challenge(ClientHandler c, String challenged) {
        for (ClientHandler aLobby : lobby) {
            if (aLobby.getClientName().equals(challenged)) {
                aLobby.sendMessage("CHALLENGEDBY " + c.getClientName());
                setCheckChallenger(c.getClientName());
            }
        }
    }

    public void challengeDecline(ClientHandler c, String challenger) {
        for (ClientHandler aLobby : lobby) {
            if (aLobby.getClientName().equals(challenger)) {
                aLobby.sendMessage("CHALLENGE_DECLINEDBY " + c.getClientName());
            }
        }
    }

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

    public void quit(ClientHandler c) {
        //TODO: remove clienthandler from the game
    }

    public void movePut(ClientHandler c, String[] blocks) {
        System.out.println(c.getGame().getCurrentPlayer().getName());
        if(c.getClientName().equals(c.getGame().getCurrentPlayer().getName())){
            for(int i = 1; i < blocks.length; i++) {
                String move = blocks[i];
                String[] tileLoc = move.split("@");
                String locTile = tileLoc[1];
                String[] coords = locTile.split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                System.out.println("int x: " + x);
                System.out.println("int y: " + y);

                Location location = new Location(x,y);

                System.out.println("Client name: " + c.getClientName());
                System.out.println("Current player: " + c.getGame().getCurrentPlayer().getName());
                System.out.println("Open locations: " + c.getGame().getBoard().getOpenLocations());
                System.out.println("Open locations size: " + c.getGame().getBoard().getOpenLocations().size());

                System.out.println("Hand of player on turn: " + c.getGame().getCurrentPlayer().getHand());
                for(int z = 0; z < c.getGame().getCurrentPlayer().getHand().size(); z++) {
                    System.out.println("Hand of player on turn: " + c.getGame().getCurrentPlayer().getHand().get(z).getId());
                }
                for (int k = 0; k < c.getGame().getBoard().getOpenLocations().size(); k++) {
                    System.out.println("Location: " + location);
                    System.out.println("GetOpenLocation.get(k): " + c.getGame().getBoard().getOpenLocations().get(k));
                    if(c.getGame().getBoard().getOpenLocations().get(k).isEqualTo(location)) {
                        for(int j = 0; j < c.getGame().getCurrentPlayer().getHand().size(); j++) {
                            Tile tile = c.getGame().getCurrentPlayer().getHand().get(j);
                            if(tile.getId() == (Integer.parseInt(tileLoc[0]))) {
                                System.out.println("Alleen de move nog leggen");

                                finalMove.put(location, tile);
                                realMove = new PutMove(finalMove);
                                System.out.println("Realmove: " + realMove);
                                System.out.println("finalMove: " + finalMove);
                                try {
                                    c.getGame().getBoard().makePutMove(realMove);
                                } catch (InvalidMoveException e) {
                                    e.printStackTrace();
                                }
                                c.getGame().getCurrentPlayer().getHand().remove(tile);
                                System.out.println("einde van de methode, alles ging goed");

                                String resultMove = "MOVEOK_PUT" + move;

                                for (int h = 0; h < c.getGame().getPlayers().size(); h++) {
                                    if (c.getGame().getPlayers().size() == 2) {
                                        twoPlayerGame.get(h).sendMessage("Move: " + move + " was successful put on the board by: " + c.getClientName());
                                    }
                                }

                            /*if (c.getGame().hasWinner()) {
                                c.getGame().printResult();
                                endGameWinner(c);
                            }
                        } *///else {
                                //invalidUserTurn(c);
                                //}

                            } else {
                            c.sendMessage("You don't own this tile"); // deze staat nog niet goed, stuurt nu bijv 3x dit bericht als de 1e 3 tiles fout zijn en dan doet hij alsnog de move
                        }
                    }
                }
            }
        }
        } else {
            c.sendMessage("It's not your turn");
        }
        c.getGame().update();
        serverView.showBoard(c.getGame().getBoard());
    }



    public void moveTrade(ClientHandler c, String[] tiles) {
        List<Tile> oldTiles = new ArrayList<>();
        List<Tile> newTiles = new ArrayList<>();
        int amount = 0;
        for (int i = 1; i < tiles.length; i++) {
            String tile = tiles[i];
            int tileId = Integer.parseInt(tile);
            c.getGame().getCurrentPlayer().getHand().remove(tileId);
            amount = tiles.length - 1;
            try {
                newTiles = c.getGame().getBoard().getPool().takeTiles(amount);
                System.out.println("NewTiles: " + newTiles);
                //oldTiles.add(tile); //oude tiles moeten omgezet worden van een String/int naar een Tile
                //c.getGame().getBoard().getPool().tradeTiles(oldTiles);
            } catch (InsufficientTilesInPoolException e) {
                e.printStackTrace();
            }
            for (Tile newTile : newTiles) {
                c.getGame().getCurrentPlayer().getHand().add(newTile);
            }
        }
        c.sendMessage("MOVEOK_TRADE" + MSG_SEPARATOR + amount);
        c.sendMessage("DRAWTILE" + MSG_SEPARATOR + newTiles);
    }

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
     * @param handler
     *            ClientHandler that will be added
     */
    public void addInactiveHandler(ClientHandler handler) {
        inactiveThreads.add(handler);
    }

} //End of class Server