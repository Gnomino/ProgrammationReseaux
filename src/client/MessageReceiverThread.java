package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiverThread extends Thread {

    Socket serverSocket;
    public MessageReceiverThread(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     *
     */
    public void run() {
        try {
            BufferedReader socIn = new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream()));
            String line;
            while(true) {
                line = socIn.readLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
