package be.ua;

import java.net.*;
import java.rmi.NotBoundException;

public class MulticastThread extends Thread{
    MulticastSocket MCsocket;
    DatagramSocket Dsocket;
    public int nodeCount = 0;
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

            while (!interrupted()) {
                byte[] buf = new byte[1000];
                DatagramPacket newMsg = new DatagramPacket(buf, buf.length);
                MCsocket.receive(newMsg);
                MCsocket.receive(newMsg);
                String msg = new String(buf, 0, newMsg.getLength());
                amountOfNodes = new BigInteger(msg).intValue();
                System.out.println(msg);
            }

            //get number of nodes via datagram
            Dsocket = new DatagramSocket(DsocketPort);
            Dsocket.setReuseAddress(true);
            byte[] buf = new byte[100];
            DatagramPacket nodeCountPacket = new DatagramPacket(buf, buf.length);
            Dsocket.receive(nodeCountPacket);
            String nodeCountS = new String(buf, 0, nodeCountPacket.getLength());
            nodeCount = Integer.parseInt(nodeCountS);
            Dsocket.close();

            //setup RMI connection
            setupRMI(name, nodeCount);

            //listen to new nodes
            while(true){
                byte[] bufN = new byte[1000];
                DatagramPacket newNode = new DatagramPacket(bufN, bufN.length);
                MCsocket.receive(newNode);
                String newNodeName = new String(bufN, 0, newNode.getLength());
                if(!newNodeName.equals(name)) {
                    listenNodeRMi(newNodeName);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setupRMI(String nodeName, int nodeCount) throws NotBoundException {
        String NodeIP = "192.168.56.1";

        RMIConnector connectorNode = new RMIConnector(INameServer, NodeIP, nodeName, nodeCount);
        INode = connectorNode.getINode();
    }

    private void listenNodeRMi(String name) {
        RMIConnector connector =new RMIConnector(INameServer, name);
        INode = connector.getINode();
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
