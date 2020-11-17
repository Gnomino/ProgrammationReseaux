package server;

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
                System.out.println("Connexion from:" + clientSocket.getInetAddress());
                ClientThread ct = new ClientThread(clientSocket);
                clientThreads.add(ct);
                ct.start();
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
        for(ClientThread client : clientThreads) {
            if(client != sender) {
                client.sendMessage(message);
            }
        }
    }
}
