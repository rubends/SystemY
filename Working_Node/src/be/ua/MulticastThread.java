package be.ua;

import java.net.*;
import java.rmi.NotBoundException;

public class MulticastThread extends Thread{
    MulticastSocket MCsocket;
    DatagramSocket Dsocket;
    public int nodeCount = 0;
    public INode INode;

    public void run() {
        String inetAddress = "224.0.1.6";
        int MulticastSocketPort = 6790;
        int DsocketPort = 6791;
        try {
            String name = "mynode";
            String ConnName = "NodeConnection";

            //join group
            InetAddress group = InetAddress.getByName(inetAddress);
            MCsocket = new MulticastSocket(MulticastSocketPort);
            MCsocket.setReuseAddress(true);
            MCsocket.joinGroup(group);

            //send node info
            DatagramPacket node = new DatagramPacket(name.getBytes(), name.length(), group, MulticastSocketPort);
            MCsocket.send(node);

            //get number of nodes
            Dsocket = new DatagramSocket(DsocketPort);
            byte[] buf = new byte[1000];
            DatagramPacket nodeCountPacket = new DatagramPacket(buf, buf.length);
            Dsocket.receive(nodeCountPacket);
            String nodeCountS = new String(buf, 0, nodeCountPacket.getLength());
            nodeCount = Integer.parseInt(nodeCountS);
            System.out.println(nodeCount);

            //setup RMI connection
            setupRMI(ConnName);

            //listen to new nodes
            while(true){
                byte[] bufN = new byte[1000];
                DatagramPacket newNode = new DatagramPacket(bufN, bufN.length);
                MCsocket.receive(newNode);
                //listenNodeRMi(1099,ConnName);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setupRMI(String ConnName) throws NotBoundException {
        String NodeIP = "192.168.1.50";

        RMIConnector connectorNode = new RMIConnector(NodeIP, ConnName);
        INode = connectorNode.getINode();
    }

    private void listenNodeRMi(int serverPort,String name) {
        RMIConnector connector =new RMIConnector(serverPort,name);
        INode = connector.getINode();
    }

    public void release() {
    }

    @Override
    public void interrupt() {
        super.interrupt();
        release();
        MCsocket.close();
        Dsocket.close();
    }
}
