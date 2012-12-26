/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

/**
 *
 * @author Asish
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class MulticastClient {

    public static void main(String[] args) throws IOException {

        MulticastSocket socket = new MulticastSocket(4446);
        InetAddress address = InetAddress.getByName("230.0.0.1");
	socket.joinGroup(address);

        DatagramPacket packet;
        String received="";
    
            // get a few quotes
	while(!received.equalsIgnoreCase("END")) {

	    byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            received = new String(packet.getData(), 0, packet.getLength());
            System.err.println(received);
	}
	socket.leaveGroup(address);
	socket.close();
    }

}
