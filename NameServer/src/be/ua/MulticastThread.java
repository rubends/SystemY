package be.ua;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastThread extends Thread{
    protected String inetAddress = "228.5.6.7";
    protected int MulticastSocket = 6789;

    String replyNodeCount = "8"; //TEST DATA
    public void run() {
        super.run();

        try {
            //open socket and join group
            InetAddress group = InetAddress.getByName(inetAddress);
            MulticastSocket MCsocket = new MulticastSocket(MulticastSocket);
            MCsocket.joinGroup(group);

            //as long as thread is not interrupted
            //while (!interrupted()) {
                //receive new node
                byte[] buf = new byte[1000];
                DatagramPacket newNode = new DatagramPacket(buf, buf.length);
                MCsocket.receive(newNode);

                InetAddress nodeIp = newNode.getAddress();
                String nodeName = new String(buf, 0, newNode.getLength());
                String addedNode = "New node name: " + nodeName + " on ip: " + nodeIp;
                System.out.println(addedNode);
/*
                //send to group
                DatagramPacket addedNodeMsg = new DatagramPacket(addedNode.getBytes(), addedNode.length(), group, MulticastSocket);
                MCsocket.send(addedNodeMsg);
*/
                //reply to new node
                byte[] b = replyNodeCount.getBytes();
                System.out.println("reply node count = " + replyNodeCount);
                //DatagramPacket howManyNodes = new DatagramPacket(b, b.length, newNode.getAddress(), newNode.getPort());
                DatagramPacket howManyNodes = new DatagramPacket(b, b.length, group, MulticastSocket);
                MCsocket.send(howManyNodes);
            //}
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
