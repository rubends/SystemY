package be.ua;

import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static INode INode;
    public static TreeMap<Integer, INode> nodeMap;

    public static void main(String[] args) {
        nodeMap = new TreeMap<>();
        RMIConnector connector = new RMIConnector();
        NameServerInterface NameServerInterface = connector.getNameServer();

        UserInterface ui = new UserInterface(NameServerInterface);

        System.out.println("\t Enter the name for the new node");
        System.out.print("> ");
        String nodeName = new Scanner(System.in).next();
        try{
            int hash = NameServerInterface.getHashOfName(nodeName);
            INode = new Node(hash, nodeMap, NameServerInterface);
        }
        catch(Exception e){}


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

    }
}

