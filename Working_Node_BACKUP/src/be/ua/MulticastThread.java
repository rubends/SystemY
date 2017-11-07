package be.ua;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.math.BigInteger;

public class MulticastThread extends Thread{
    protected String inetAddress = "228.5.6.7";
    protected int MulticastSocket = 6789;
    protected int amountOfNodes = 0; //receive from nameserver

    public void run() {
        try {

            String name = "SecondNode";

            //join group
            InetAddress group = InetAddress.getByName(inetAddress);
            java.net.MulticastSocket MCsocket = new MulticastSocket(MulticastSocket);
            MCsocket.joinGroup(group);

            //send node info
            DatagramPacket node = new DatagramPacket(name.getBytes(), name.length(), group, MulticastSocket);
            MCsocket.send(node);
            //while (!interrupted()) {
                byte[] buf = new byte[1000];
                DatagramPacket newMsg = new DatagramPacket(buf, buf.length);
                MCsocket.receive(newMsg);
                MCsocket.receive(newMsg);
                String msg = new String(buf, 0, newMsg.getLength());
                amountOfNodes = new BigInteger(msg).intValue();
                System.out.println(msg);
            //}

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getAmountOfNodes() {
        return amountOfNodes;
    }

    public void release() {
    }

    @Override
    public void interrupt() {
        super.interrupt();
        release();
    }
}
