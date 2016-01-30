package Model.Network;

/**
 * Created by Herjan on 20-1-2016.
 */
import Model.Game.ServerGame;
import View.ClientTUI;
import View.ClientView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Herjan on 20-1-2016.
 */
public class Client extends Thread {

    private static final String USAGE = "-- ENTER CREDENTIALS --";

    public static String clientNom;
    public static String clientHostAd;
    public static String clientPort;
    private static ClientView clientView;
    public ServerGame newGame;

    /** Start een Client-applicatie op. */
    public static void main(String[] args) {
        clientView = new ClientTUI();
        clientView.showMessage(USAGE);
        clientView.showMessage("");
        Scanner in = new Scanner(System.in);
        clientView.showMessage("Enter name: ");
        clientNom = in.nextLine();
        clientView.showMessage("Enter host-address: ");
        clientHostAd = in.nextLine();
        clientView.showMessage("Enter portnumber: ");
        clientPort = in.nextLine();

        InetAddress host = null;
        int port = 0;

        try {
            host = InetAddress.getByName(clientHostAd);
        } catch (UnknownHostException e) {
            clientView.showMessage("ERROR: no valid hostname!");
            System.exit(0);

        }

        try {
            port = Integer.parseInt(clientPort);
        } catch (NumberFormatException e) {
            clientView.showMessage("ERROR: no valid portnumber!");
            System.exit(0);
        }

        try {
            Client client = new Client(clientNom, host, port);
            client.setName(clientNom);
            client.start();

            do {
                String input = readString("");
                client.sendMessage(input);
            } while (true);

        } catch (IOException e) {
            clientView.showMessage("ERROR: couldn't construct a client object!");
            System.exit(0);
        }

    }

    private String clientName;
    private Socket sock;
    private BufferedReader in;
    private BufferedWriter out;

    private List<String> clientFeatures = new ArrayList<>(); //TODO: add clientFeatures
    private List<String> serverFeatures = new ArrayList<>();

    /**
     * Constructs a Client-object and tries to make a socket connection
     */
    public Client(String name, InetAddress host, int port) throws IOException {
        clientName = name;
        sock = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

    }

    private String msgSeparator = " ";

    /**
     * Reads the messages in the socket connection. Each message will be
     * forwarded to the MessageUI
     */
    public void run() {
        clientFeatures.add("LOBBY");
        clientFeatures.add("CHALLENGE");
        String IDENTIFY = "IDENTIFY";
        this.sendMessage(IDENTIFY + msgSeparator + this.getClientName() + msgSeparator + clientFeatures);
        clientFeatures.clear();
        clientView.showMessage("Hallo"+ msgSeparator +clientName);
        try {
            String message = in.readLine();
            String[] blocks = message.split(msgSeparator);
            while (message != null) {
                clientView.showMessage("Command from server: " + message);

                switch(blocks[0]) {
                    case "IDENTIFYOK": identify(blocks); break;
                    case "CHALLENGEDBY": challenge(blocks); break;
                    case "CHALLENGEFAIL": challengeFail(); break;
                    case "CHALLENGE_DECLINEDBY": challengeDecline(blocks); break;
                    case "WRONGNUMBER": wrongNumber(blocks);break;
                    case "GAMESTART": startGame(blocks);break;
                    case "MOVEOK_PUT": moveOk();break;
                }

                message = in.readLine();
                blocks = message.split(msgSeparator);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        shutdown();
    }

    /** returns the client name */
    public String getClientName() {
        return clientName;
    }


    public void identify(String [] blocks){
        serverFeatures.addAll(Arrays.asList(blocks).subList(1, blocks.length));
        clientView.showMessage("IDENTIFYOK" + msgSeparator + serverFeatures);
    }

    public void challenge(String [] blocks){
        clientView.showMessage("You're challenged by " + blocks[1]);
    }

    public void challengeDecline(String [] blocks) {
        clientView.showMessage("Your challenge is declined by " + blocks[1]);
    }

    public void wrongNumber(String [] blocks) {
        clientView.showMessage("Choose a valid number: 2, 3 or 4");
    }

    public void startGame(String [] blocks) {
        String [] playersList = new String[blocks.length - 2];
        for(int i = 0; i < blocks.length - 2; i++) {
            playersList[i] = blocks[i + 2];
        }
        String name1 = blocks[1];
        newGame = new ServerGame(playersList, name1);
        clientView.showBoard(newGame.getBoard());
    }

    public void moveOk() {
        clientView.showMessage("Move was successful put on the board");
    }

    public void challengeFail() {
        clientView.showMessage("Couldn't challenge player");
    }

    /** send a message to a ClientHandler. */
    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            shutdown();
            e.printStackTrace();
        }
    }

    /** close the socket connection. */
    public void shutdown() {
        try {
            sock.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String text) {
        clientView.showMessage(text);
        String answer = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            answer = in.readLine();
        } catch (IOException e) {
        }

        return (answer == null) ? "" : answer;
    }
} //End of class Client
