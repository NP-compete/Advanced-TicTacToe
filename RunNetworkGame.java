import java.net.Socket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.util.*;
import java.io.IOException;

public class RunNetworkGame{


    public static void runAsHost(){

        Socket client = createClientConnection();
        TicTacticsBoard board = new TicTacticsBoard();
        BoardPrinter printer = new BoardTextPrinter(board);
        GamePlayer player1 = new NetworkPlayer(1, true, client);
        GamePlayer player2 = new NetworkPlayer(-1, false, client);
        TicTactics game = new TicTactics(board, printer, player1, player2);
        game.run();
    }

    private static Socket createClientConnection(){

        try {
            ServerSocket listener = new ServerSocket(0);
            InetAddress ip = InetAddress.getLocalHost();
            String ipAndPort = ip.getHostAddress() + ":" + listener.getLocalPort();
            System.out.println("waiting a client to connect on : " 
                                + ipAndPort);
            Socket client = listener.accept();
            listener.close();
            String cl_address = client.getRemoteSocketAddress().toString();
            System.out.println("connected to the client " + cl_address); 
            return client; 
        }
        catch ( IOException e){
            System.out.println("Cannot start a server!");
            System.exit(0);
        }
        finally{
        }
        return null;
    }

    public static void runAsGuest(){

        Socket server = createServerConnection();
        TicTacticsBoard board = new TicTacticsBoard();
        BoardPrinter printer = new BoardTextPrinter(board);
        GamePlayer player1 = new NetworkPlayer(1, false, server);
        GamePlayer player2 = new NetworkPlayer(-1, true, server);
        TicTactics game = new TicTactics(board, printer, player1, player2);
        game.run();
    }

    private static Socket createServerConnection(){

        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("enter host address(ip:port) : ");
            String ans = sc.nextLine();
            String[] tokens = ans.trim().split(":");
            String ip = tokens[0].trim();
            int port = Integer.parseInt( tokens[1] );
            Socket server = new Socket(ip, port);
            String server_address = server.getRemoteSocketAddress().toString();
            System.out.println("connected to the server " + server_address); 
            return server;
        }
        catch ( IOException e){
            System.out.println("Cannot start a client!");
        }
        return null;
    }


    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        int gametype = -1;
        while ( gametype != 1 && gametype != 2 ){
            System.out.println("Choose game type: 1-Server or 2-Client");
            String ans = sc.nextLine();
            gametype = Integer.parseInt(ans);
        } 
        if ( gametype == 1 ) runAsHost();
        else runAsGuest(); 
    }

}
