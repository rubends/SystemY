package be.ua;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastThread extends Thread{
    protected String inetAddress = "228.5.6.7";
    protected int MulticastSocket = 6789;


    public void run() {
        super.run();

        try {
            //open socket and join group
            InetAddress group = InetAddress.getByName(inetAddress);
            MulticastSocket MCsocket = new MulticastSocket(MulticastSocket);
            MCsocket.joinGroup(group);

            //as long as thread is not interrupted
            while (!interrupted()) {
                //receive new node
                byte[] buf = new byte[1000];
                DatagramPacket newNode = new DatagramPacket(buf, buf.length);
                MCsocket.receive(newNode);

                InetAddress nodeIp = newNode.getAddress();
                String nodeName = new String(buf, 0, newNode.getLength());
                String addedNode = "New node name: " + nodeName + " on ip: " + nodeIp;
                System.out.println(addedNode);

                //send to group
                DatagramPacket addedNodeMsg = new DatagramPacket(addedNode.getBytes(), addedNode.length(), group, MulticastSocket);
                MCsocket.send(addedNodeMsg);

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
