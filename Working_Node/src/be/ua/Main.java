package be.ua;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static INode INode;
    public static String ipNameServer = "169.254.234.139"; //127.0.0.1

    public static void main(String[] args) {
        RMIConnector connector = new RMIConnector();
        NameServerInterface NameServerInterface = connector.getNameServer();

        UserInterface ui = new UserInterface(NameServerInterface);
        System.out.println("\t Enter the name for the new node");
        System.out.print("> ");
        String nodeName = new Scanner(System.in).next();
        try{
            int hash = NameServerInterface.getHashOfName(nodeName);
            INode = new Node(hash, NameServerInterface);
            ArrayList<Integer> ids = NameServerInterface.getNeighbourNodes(hash); //get own neighbours
            INode.updateNeighbours(ids.get(0), ids.get(1));
            new RMIConnector(NameServerInterface, nodeName, INode);
        }
        catch(Exception e){e.printStackTrace();}

        MulticastThread multicastThread = new MulticastThread(nodeName, NameServerInterface);
        multicastThread.start();

        TCPReceiverThread tcpReceiverThread = new TCPReceiverThread();
        tcpReceiverThread.start();
        UpdateFileMapThread updateFileMapThread = new UpdateFileMapThread(nodeName, NameServerInterface);
        updateFileMapThread.start();

        Replication replication = new Replication(NameServerInterface);
        replication.createFicheOnStartup();// Moet voor get files gebeuren
        replication.setNodeName(nodeName);
        replication.getFiles();


        //implementation of agents

        FileAgent fileAgent = new FileAgent();
        try {
            RMIAgentInterface rmiAgent = new RMIAgent(INode, NameServerInterface);
            //System.out.println(rmiAgent);
            //output from rmiAgent: RMIAgent{nextNodeId=15324, myNodeId=21591, ns=Proxy[NameServerInterface,RemoteObjectInvocationHandler
            // [UnicastRef [liveRef: [endpoint:[169.254.234.139:49167](remote),objID:[5ce6daa0:1607db91471:-7fff, -7627599808127725904]]]]]}

            /*
            connector.bindRMIAgent(rmiAgent);
            rmiAgent.passFileAgent(fileAgent);
            */

        } catch (Exception e) {
            System.out.println("Agents created error:");
            e.printStackTrace();
        }

        ui.startUI();

    }
}

