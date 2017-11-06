package be.ua;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
//----------------------------------
import java.lang.Object;
import java.lang.Number;
import java.math.BigInteger;
//-----------------------------------

public class MulticastThread extends Thread{
    protected String inetAddress = "228.5.6.7";
    protected int MulticastSocket = 6789;
    protected int amountOfNodes = 0; //receive from nameserver

    public void run() {
        try {

            String name = "myname";

            //----------------------------------
            byte[] buffer = new byte[1000];
            DatagramPacket nodeCount  = new DatagramPacket(buffer, buffer.length);
            //----------------------------------

            //join group
            InetAddress group = InetAddress.getByName(inetAddress);
            java.net.MulticastSocket MCsocket = new MulticastSocket(MulticastSocket);
            MCsocket.joinGroup(group);

            //send node info
            DatagramPacket node = new DatagramPacket(name.getBytes(), name.length(), group, MulticastSocket);
            MCsocket.send(node);

            //----------------------------------
            //Receive the node count
            //CHECKEN WANT IS VREEMD
            MCsocket.receive(nodeCount);//buffer cleared??!
            MCsocket.receive(nodeCount);
            String ndCount = new String(buffer, 0, nodeCount.getLength());
            System.out.println("received from server: " + ndCount);
            amountOfNodes = new BigInteger(ndCount).intValue();
            System.out.println("conversion to int: " + amountOfNodes);
            //----------------------------------

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getAmountOfNodes() {
        return amountOfNodes;
    }

}
