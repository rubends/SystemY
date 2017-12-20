package be.ua;

import java.util.Scanner;

public class Main {
    public static INode INode;
    public static String ipNameServer = "169.254.130.0"; //127.0.0.1

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

        FileAgent fileAgent = new FileAgent();
        try {
            //RMIAgent rmiAgent = new RMIAgent(INode, NameServerInterface);
            //rmiAgent.passFileAgent(fileAgent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ui.startUI();
    }
}

