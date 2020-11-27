package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    /**
     * The main method establishes a connection to the server, created a MessageReceiverThread
     * and listens for user input to send their messages to the server
     **/
    public static void main(String[] args) throws IOException {

        Socket socket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;
        MessageReceiverThread messageReceiverThread;

        if (args.length != 3) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port> <Username>");
            System.exit(1);
        }

        try {
            // creation socket ==> connexion
            socket = new Socket(args[0], Integer.parseInt(args[1]));

            socOut= new PrintStream(socket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            socOut.println(args[2]);
            messageReceiverThread = new MessageReceiverThread(socket);
            messageReceiverThread.start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        String line;
        while (true) {
            line=stdIn.readLine();
            if (line.equals(".")) break;
            socOut.println(line);
        }

        stdIn.close();
        socket.close();
    }
}
