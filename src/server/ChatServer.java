package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    /**
     * The list of threads for connected clients
     */
    protected static List<ClientThread> clientThreads = new ArrayList<>();

    /**
     * The listening port of the chat server
     */
    protected static final int PORT = 1234;

    /**
     * The path to the message history file
     * The file will be created if it doesn't already exist
     */
    protected static final String HISTORY_FILE = "message_log.txt";

    public static void main(String args[]){
        ServerSocket listenSocket;

        try {
            listenSocket = new ServerSocket(PORT);
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connection from:" + clientSocket.getInetAddress());
                ClientThread ct = new ClientThread(clientSocket);
                clientThreads.add(ct);
                ct.start();
                sendHistory(ct);
            }
        } catch (Exception e) {
            System.err.println("Error in Server:");
            e.printStackTrace();
        }
    }

    /**
     * Processes a received message and sends it to other clients
     * @param message
     * @param sender
     */
    public static void sendMessage(String message, ClientThread sender) {
        String outputMessage = "[" + sender.getUsername() + "] " + message;
        for(ClientThread client : clientThreads) {
            if(client != sender) {
                client.sendMessage(outputMessage);
            }
        }
        saveMessage(outputMessage);
    }

    /**
     * Appends a message to the log file
     * @param message the message to save
     */
    private static void saveMessage(String message) {
        try (FileWriter fw = new FileWriter(HISTORY_FILE, true)) {
            fw.write(message);
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the whole history of messages to a client
     * @param client A client that has just joined
     */
    private static void sendHistory(ClientThread client) {
        try (FileInputStream fis = new FileInputStream(HISTORY_FILE)) {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = fis.read(buffer)) > 0) {
                client.sendData(buffer, count);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Warning: the history file does not exist yet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to everyone notifying them of the user joining
     * @param client The client that has joined
     */
    public static void sendJoinMessage(ClientThread client) {
        String joinMessage = client.getUsername() + " has just joined the chat !";
        saveMessage(joinMessage);
        for (ClientThread clientThread : clientThreads) {
            clientThread.sendMessage(joinMessage);
        }
    }

    /**
     * Removes a client thread from the list of active sockets and sends a leave message
     * @param client The client thread to remove
     */
    public static void deregisterClient(ClientThread client) {
        clientThreads.remove(client);
        String leaveMessage = client.getUsername() + " has just left the chat.";
        saveMessage(leaveMessage);
        for(ClientThread clientThread : clientThreads) {
            clientThread.sendMessage(leaveMessage);
        }
    }
}
