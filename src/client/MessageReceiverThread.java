package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This threads listens for new messages from the server
 * and prints them to the standard output
 * @author Thomas Buresi & Sylvain Vaure
 */
public class MessageReceiverThread extends Thread {
    /**
     * The socket connecting the client and the chat server
     */
    Socket serverSocket;
    public MessageReceiverThread(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        try {
            BufferedReader socIn = new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream()));
            String line;
            while(!serverSocket.isClosed()) {
                line = socIn.readLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Connection ended");
        }
    }
}
