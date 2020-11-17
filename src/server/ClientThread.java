package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket clientSocket;

    ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Receives a message from client then passes it to the main thread
     **/
    public void run() {
        try {
            BufferedReader socIn = null;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                String line = socIn.readLine();
                ChatServer.sendMessage(line, this);
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    /**
     * Sends a message to the socket of this client
     * @param message
     */
    public void sendMessage(String message) {
        try {
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            socOut.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
