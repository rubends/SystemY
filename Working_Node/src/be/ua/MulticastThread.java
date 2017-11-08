package be.ua;

import java.net.*;

public class MulticastThread extends Thread{
    MulticastSocket MCsocket;

    public void run() {
        String inetAddress = "224.0.1.6";
        int MulticastSocket = 6790;
        try {
            String name = "mynode";
            //join group
            InetAddress group = InetAddress.getByName(inetAddress);
            MCsocket = new MulticastSocket(MulticastSocket);
            MCsocket.setReuseAddress(true);
            MCsocket.joinGroup(group);

            //send node info
            DatagramPacket node = new DatagramPacket(name.getBytes(), name.length(), group, MulticastSocket);
            MCsocket.send(node);

            //get number of nodes
            byte[] buf = new byte[1000];
            DatagramPacket nodeCountPacket = new DatagramPacket(buf, buf.length);
            MCsocket.receive(nodeCountPacket); //GETS THE NAME OF THE NODE
            MCsocket.receive(nodeCountPacket); // GET NODECOUNT

            String nodeCount = new String(buf, 0, nodeCountPacket.getLength());
            System.out.println(nodeCount);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
    }

    @Override
    public void interrupt() {
        super.interrupt();
        release();

        MCsocket.close();
    }
}
