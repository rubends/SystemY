package be.ua;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class MulticastThread extends Thread{
    private NameServer nameServer;
    private String inetAddress = "228.5.6.7";
    private int MulticastSocket = 6789;

    public MulticastThread(NameServer ns) {
        nameServer = ns;
    }

    public void run() {
        try {
            //as long as thread is not interrupted
            while (!interrupted()) {
                //open socket and join group
                InetAddress group = InetAddress.getByName(inetAddress);
                MulticastSocket MCsocket = new MulticastSocket(MulticastSocket);
                MCsocket.joinGroup(group);

                //receive new node
                byte[] buf = new byte[1000];
                DatagramPacket newNode = new DatagramPacket(buf, buf.length);
                MCsocket.receive(newNode);

                InetAddress nodeIp = newNode.getAddress();
                String nodeName = new String(buf, 0, newNode.getLength());
                //System.out.println("New node name: " + nodeName + " on ip: " + nodeIp);

                nameServer.addNode(nodeName, nodeIp.toString());

                //get number of nodes in network
                int nodeCount = nameServer.getNodeCount();
                byte [] nodeCountBuf = ByteBuffer.allocate(4).putInt(nodeCount).array();
                DatagramPacket nodecountPacket = new DatagramPacket(nodeCountBuf, nodeCountBuf.length, nodeIp, MulticastSocket);
                MCsocket.send(nodecountPacket);
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
    }
}
