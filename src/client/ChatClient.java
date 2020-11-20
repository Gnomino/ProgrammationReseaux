package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

public class ChatClient {
    /**
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) throws IOException {

        MulticastSocket socket = null;
        MessageReceiverThread messageReceiverThread;
        BufferedReader stdIn = null;
        stdIn = new BufferedReader(new InputStreamReader(System.in));

        if (args.length != 2) {
            System.out.println("Usage: java ChatClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        InetAddress groupAddr = null;
        int groupPort = Integer.parseInt(args[1]);
        try {
            // creation socket ==> connexion
            groupAddr = InetAddress.getByName(args[0]);
            socket = new MulticastSocket(groupPort);
            socket.joinGroup(groupAddr);

            messageReceiverThread = new MessageReceiverThread(socket);
            messageReceiverThread.start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println(args[0] + " ; " + groupAddr + " ; " + groupPort);
            e.printStackTrace();
            System.exit(1);
        }

        String line;
        while (true) {
            line=stdIn.readLine();
            if (line.equals(".")) break;
            DatagramPacket packet = new DatagramPacket(line.getBytes(), line.length(), groupAddr, groupPort);
            socket.send(packet);
        }

        stdIn.close();
        socket.close();
    }
}
