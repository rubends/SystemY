package be.ua;

import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MulticastThread extends Thread{
    MulticastSocket MCsocket;
    DatagramSocket Dsocket;
    public static int nodeCount = 0;
    public INode INode;

    String name;
    NameServerInterface INameServer;

    public MulticastThread(String nodeName, NameServerInterface ns) {
        name = nodeName;
        INameServer = ns;
    }

    public void run() {
        String inetAddress = "224.0.1.6";
        int MulticastSocketPort = 6790;
        int DsocketPort = 6791;
        try {
            //join group
            InetAddress group = InetAddress.getByName(inetAddress);
            MCsocket = new MulticastSocket(MulticastSocketPort);
            MCsocket.setReuseAddress(true);
            MCsocket.joinGroup(group);

            //send node info
            DatagramPacket node = new DatagramPacket(name.getBytes(), name.length(), group, MulticastSocketPort);
            MCsocket.send(node);

            //get number of nodes via datagram
            Dsocket = new DatagramSocket(DsocketPort);
            Dsocket.setReuseAddress(true);
            byte[] buf = new byte[100];
            DatagramPacket nodeCountPacket = new DatagramPacket(buf, buf.length);
            Dsocket.receive(nodeCountPacket);
            String nodeCountS = new String(buf, 0, nodeCountPacket.getLength());
            nodeCount = Integer.parseInt(nodeCountS);
            Dsocket.close();
            //listen to new nodes
            while(true){
                byte[] bufN = new byte[1000];
                DatagramPacket newNode = new DatagramPacket(bufN, bufN.length);
                MCsocket.receive(newNode);
                String newNodeName = new String(bufN, 0, newNode.getLength());
                if(!newNodeName.equals(name)) {
                    getNewNodeRMi(newNodeName);
                }
            }
        } catch(Exception e) {
            MCsocket.close();
            e.printStackTrace();
        }
    }

    private void getNewNodeRMi(String newName) {
        try{
            new RMIConnector(INameServer, newName);
        }
        catch (RemoteException e){
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
