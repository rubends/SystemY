package be.ua;

import java.net.*;

public class MulticastThread extends Thread{
    private NameServerInterface nameServer;
    MulticastSocket MCsocket;
    DatagramSocket Dsocket;

    public MulticastThread(NameServerInterface ns) {
        nameServer = ns;
    }

    public void run() {
        String inetAddress = "224.0.1.6";
        int MulticastSocketPort = 6790;
        int DsocketPort = 6791;

        try {

            //as long as thread is not interrupted
            while (!interrupted()) {
                //open socket and join group
                InetAddress group = InetAddress.getByName(inetAddress);
                MCsocket = new MulticastSocket(MulticastSocketPort);
                MCsocket.setReuseAddress(true);
                MCsocket.joinGroup(group);

                //receive new node
                byte[] buf = new byte[1000];
                DatagramPacket newNode = new DatagramPacket(buf, buf.length);
                MCsocket.receive(newNode);

                InetAddress nodeIp = newNode.getAddress();
                String nodeName = new String(buf, 0, newNode.getLength());
                System.out.println("New node name: " + nodeName + " on ip: " + nodeIp);

                nameServer.addNode(nodeName, nodeIp.getHostAddress());

                //get number of nodes in network
                Dsocket = new DatagramSocket();
                String nodeCount = Integer.toString(nameServer.getNodeCount());
                DatagramPacket nodecountPacket = new DatagramPacket(nodeCount.getBytes(), nodeCount.length(), nodeIp, DsocketPort);
                Dsocket.send(nodecountPacket);
            }
        } catch(Exception e) {
            if(interrupted()) {
                //thread stopped by user.
            } else
                e.printStackTrace();
            release();
        }

    }

    public void release() {
    }

    @Override
    public void interrupt() {
        super.interrupt();
        release();
        Dsocket.close();
        MCsocket.close();
    }
}
