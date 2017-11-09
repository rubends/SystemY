package be.ua;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.*;

public class MulticastThread extends Thread{
    protected String inetAddress = "228.5.6.7";
    protected int MulticastSocket = 6789;
    protected int amountOfNodes = 0; //receive from nameserver //FOR TEST
    public INode INode;

    public void run() {
        try {

            String name = "FirstNode";

            //join group
            InetAddress group = InetAddress.getByName(inetAddress);
            java.net.MulticastSocket MCsocket = new MulticastSocket(MulticastSocket);
            MCsocket.joinGroup(group);

            //send node info
            DatagramPacket node = new DatagramPacket(name.getBytes(), name.length(), group, MulticastSocket);
            MCsocket.send(node);

            String name1 = "NodeConnection";

            while (!interrupted()) {
                byte[] buf = new byte[1000];
                DatagramPacket newMsg = new DatagramPacket(buf, buf.length);
                //MCsocket.receive(newMsg);
                MCsocket.receive(newMsg);
                String msg = new String(buf, 0, newMsg.getLength());
                //amountOfNodes = new BigInteger(msg).intValue();
                System.out.println(msg);
            }

            try{
                setupRMI(1099,name1);//COMM START WITH OTHER NODE
                //int RETURN = INode.getPreviousNode();
                //System.out.println("getting return from other node '" + RETURN);
            }
            catch(NotBoundException nb){
                System.out.println("NOT BOUND!");
            }





        }
        catch(IOException e){

        }
    }


    public int getAmountOfNodes() {
        return amountOfNodes;
    }

    private void setupRMI(int serverPort,String name) throws NotBoundException {
        String NodeIP = "192.168.1.50";
        String rmiName = "TEST";

        RMIConnector connectorNode =new RMIConnector(NodeIP,rmiName,5);
        INode = connectorNode.getINode();

    }


    public void release() {
    }

    @Override
    public void interrupt() {
        super.interrupt();
        release();
    }
}
