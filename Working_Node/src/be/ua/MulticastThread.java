package be.ua;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastThread extends Thread{
    protected String inetAddress = "228.5.6.7";
    protected int MulticastSocket = 6789;
    protected int amountOfNodes = 0; //receive from nameserver

    public void run() {
        try {

            String name = "myname";

            //join group
            InetAddress group = InetAddress.getByName(inetAddress);
            MulticastSocket MCsocket = new MulticastSocket(MulticastSocket);
            MCsocket.joinGroup(group);
            //MCsocket.setLoopbackMode(true);

            //send node info
            DatagramPacket node = new DatagramPacket(name.getBytes(), name.length(), group, MulticastSocket);
            MCsocket.send(node);
            while (!interrupted()) {
                byte[] buf = new byte[1000];
                DatagramPacket nodeCountPacket = new DatagramPacket(buf, buf.length);
                MCsocket.receive(nodeCountPacket);

                String nodeCount = new String(buf, 0, nodeCountPacket.getLength());
                System.out.println(nodeCount);
            }

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
