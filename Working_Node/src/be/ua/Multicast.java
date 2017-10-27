package be.ua;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Multicast extends Thread{
    protected String inetAddress = "228.5.6.7";
    protected int MulticastSocket = 6789;

    public void Connect() {
        try {
            String name = "myname";

            //join group
            InetAddress group = InetAddress.getByName(inetAddress);
            java.net.MulticastSocket MCsocket = new MulticastSocket(MulticastSocket);
            MCsocket.joinGroup(group);

            //send node info
            DatagramPacket node = new DatagramPacket(name.getBytes(), name.length(), group, MulticastSocket);
            MCsocket.send(node);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
