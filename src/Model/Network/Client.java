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
            System.out.println(clientNom);
            System.out.println(host);
            System.out.println(port);
            Client client = new Client(clientNom, host, port);
            client.sendMessage(clientNom);
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
        this.sendMessage("Hallo"+msgSeperator+clientName);
        System.out.println("Waiting for other client...");
        try {
            System.out.println("nope");
            String message = in.readLine();
            System.out.println("Hallo" + message + "Doei");
            String[] blocks = message.split(msgSeperator);
            while (message != null) {
                print("Command from server: " + message);

                switch(blocks[0]) {
                    case "hallo": System.out.println("Doei"); break;
                    case "IDENTIFY": identify(blocks); break;
                    //case sendBoard: sendBoard(blocks); break;
                    //case startGame: startGame(blocks); break;
                    //case moveResult: moveResult(blocks); break;
                }

                message = in.readLine();
                blocks = message.split(msgSeperator);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("WHUT");
        shutdown();
    }

    /** returns the client name */
    public String getClientName() {
        return clientName;
    }

    List<String> features = new ArrayList<String>();

    public void identify(String [] blocks){
        System.out.println("IDENTIFYOK" + msgSeperator + features);

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
