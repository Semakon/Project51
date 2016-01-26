package Model.Network;

/**
 * Created by Herjan on 20-1-2016.
 */
import Model.Game.Game;
import Model.IProtocol;

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
    public Game newGame;

    /** Start een Client-applicatie op. */
    public static void main(String[] args) {
        System.out.println(USAGE);
        System.out.println("");
        Scanner in = new Scanner(System.in);
        System.out.println("Enter name: ");
        clientNom = in.nextLine();
        System.out.println("Enter host-address: ");
        clientHostAd = in.nextLine();
        System.out.println("Enter portnumber: ");
        clientPort = in.nextLine();

        InetAddress host = null;
        int port = 0;

        try {
            host = InetAddress.getByName(clientHostAd);
        } catch (UnknownHostException e) {
            print("ERROR: no valid hostname!");
            System.exit(0);

        }

        try {
            port = Integer.parseInt(clientPort);
        } catch (NumberFormatException e) {
            print("ERROR: no valid portnumber!");
            System.exit(0);
        }

        try {
            Client client = new Client(clientNom, host, port);
            client.setName(clientNom);;
            client.start();

            do {
                String input = readString("");
                client.sendMessage(input);
            } while (true);

        } catch (IOException e) {
            print("ERROR: couldn't construct a client object!");
            System.exit(0);
        }

    }

    private String clientName;
    private Socket sock;
    private BufferedReader in;
    private BufferedWriter out;

    /**
     * Constructs a Client-object and tries to make a socket connection
     */
    public Client(String name, InetAddress host, int port) throws IOException {
        clientName = name;
        sock = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

    }

    String msgSeperator = " ";
    String IDENTIFY = "IDENTIFY";

    /**
     * Reads the messages in the socket connection. Each message will be
     * forwarded to the MessageUI
     */
    public void run() {
        clientFeatures.add("LOBBY");
        clientFeatures.add("CHALLENGE");
        this.sendMessage(IDENTIFY + msgSeperator + this.getClientName() + msgSeperator + clientFeatures);
        clientFeatures.clear();
        System.out.println("Hallo"+msgSeperator+clientName);
        try {
            String message = in.readLine();
            String[] blocks = message.split(msgSeperator);
            while (message != null) {
                print("Command from server: " + message);

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
                blocks = message.split(msgSeperator);
            }
        } catch (IOException e) {
            e.printStackTrace();
        };
        shutdown();
    }

    /** returns the client name */
    public String getClientName() {
        return clientName;
    }

    List<String> clientFeatures = new ArrayList<String>(); //TODO: clientFeatures toevoegen
    List<String> serverFeatures = new ArrayList<String>();

    public void identify(String [] blocks){
        for(int i = 1; i < blocks.length; i++) {
            serverFeatures.add(blocks[i]);
        }
        System.out.println("IDENTIFYOK" + msgSeperator + serverFeatures);
    }

    public void challenge(String [] blocks){
        System.out.println("You're challenged by " + blocks[1]);
    }

    public void challengeDecline(String [] blocks) {
        System.out.println("Your challenge is declined by " + blocks[1]);
    }

    public void wrongNumber(String [] Blocks) {
        System.out.print("Choose a valid number: 2, 3 or 4");
    }

    public void startGame(String [] blocks) {
        String [] playersList = new String[blocks.length - 2];
        for(int i = 0; i < blocks.length - 2; i++) {
            playersList[i] = blocks[i + 2];
        }
        String name1 = blocks[1];
        newGame = new Game(name1, playersList, name1);
        System.out.println(newGame.getBoard().toString());
    }

    public void moveOk() {
        System.out.println("Move was successful put on the board");
    }

    public void challengeFail() {
        System.out.println("Couldn't challenge player");
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

    private static void print(String message) {
        System.out.println(message + "\n");
    }

    public static String readString(String tekst) {
        System.out.print(tekst);
        String antw = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            antw = in.readLine();
        } catch (IOException e) {
        }

        return (antw == null) ? "" : antw;
    }
} //End of class Client
