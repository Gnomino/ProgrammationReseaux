package client;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MessageReceiverThread extends Thread {

    MulticastSocket socket;
    public MessageReceiverThread(MulticastSocket socket) {
        this.socket = socket;
    }

    /**
     *
     */
    public void run() {
        byte[] buf = new byte[1000];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        while(true) {
            try {
                socket.receive(p);
                String message = new String(buf, 0, p.getLength());
                System.out.println("[" + p.getAddress() + "] " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
