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
            byte[] buf = new byte[1000];

            //as long as thread is not interrupted
            while (!interrupted()) {
                //open socket and join group
                InetAddress group = InetAddress.getByName(inetAddress);
                MulticastSocket MCsocket = new MulticastSocket(MulticastSocket);
                MCsocket.joinGroup(group);

                //receive new node
                DatagramPacket newNode = new DatagramPacket(buf, buf.length);
                MCsocket.receive(newNode);

                InetAddress nodeIp = newNode.getAddress();
                String nodeName = new String(buf, 0, newNode.getLength());
                System.out.println("new node name: " + nodeName + " on ip: " + nodeIp);

                //-----------------------
                /*
                //send to group
                String msg = "Hello";
                DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, MulticastSocket);
                MCsocket.send(hi);
                System.out.println("msg = " + hi.getData());
                */
                //-----------------------

                //-------------------- FOR NODE COUNT REPLY-----------
                // WORDT NOG OP MULTICAST VERZONDEN EN NIET OP IP??
                byte[] b = replyNodeCount.getBytes();
                System.out.println("reply node count = " + replyNodeCount);
                //DatagramPacket howManyNodes = new DatagramPacket(b, b.length, newNode.getAddress(), newNode.getPort());
                DatagramPacket howManyNodes = new DatagramPacket(b, b.length, group, MulticastSocket);
                MCsocket.send(howManyNodes);
                //---------------------- END OF NODE COUNT REPLY-----------
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
