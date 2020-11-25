package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private static List<ClientThread> clientThreads = new ArrayList<>();

    public static void main(String args[]){
        ServerSocket listenSocket;

        try {
            listenSocket = new ServerSocket(1234); //port
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
        try (FileWriter fw = new FileWriter("message_log.txt", true)) {
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
        try (FileInputStream fis = new FileInputStream("message_log.txt")) {
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
}
