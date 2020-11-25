package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket clientSocket;
    private String username;
    private PrintStream socOut;

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
            socOut = new PrintStream(clientSocket.getOutputStream());
            username = socIn.readLine();
            ChatServer.sendJoinMessage(this);
            while (!clientSocket.isClosed()) {
                String line = socIn.readLine();
                if(line != null) {
                    ChatServer.sendMessage(line, this);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
        ChatServer.deregisterClient(this);
    }

    /**
     * Sends a message to the socket of this client
     * @param message
     */
    public void sendMessage(String message) {
        socOut.println(message);
    }

    /**
     * @return The username of this client
     */
    public String getUsername() {
        return username;
    }

    /**
     * Send raw bytes to the client
     * @param data The bytes to send
     * @param count The number of bytes to send
     */
    public void sendData(byte[] data, int count) {
        try {
            clientSocket.getOutputStream().write(data, 0, count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
