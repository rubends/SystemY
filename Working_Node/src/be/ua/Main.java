package be.ua;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    //global variables
    public static String recommendedNameServer = "192.168.0.170";
    public static String ipNameServer = "";
    public static INode INode;
    public static String rootPath = new File("").getAbsolutePath();
    public static String sep = System.getProperty("file.separator"); //OS dependable
    public static String pathToFiles = rootPath + sep + "Files" + sep;
    public static String pathToLocalFiles = rootPath + sep + "Files" + sep + "Local" + sep;
    public static String pathToReplFiles = rootPath + sep + "Files" + sep + "Replication" + sep;
    public static NameServerInterface NameServerInterface;
    public static Controller controller;
    private static FileAgent fileAgent;
    public static String nodeName = "";
    public static void main(String[] args) {

        View viewPanel1 = new View(true);
        controller = new Controller(true, null);
        controller.createListeners(viewPanel1);
        viewPanel1.setVisible(true);
        while(nodeName == ""){
            nodeName = nodeName;
        }
        viewPanel1.setVisible(false);

        RMIConnector connector = new RMIConnector();
        NameServerInterface = connector.getNameServer();
        UserInterface ui = new UserInterface(NameServerInterface);
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
        (new Thread(){ // keep going without interrupting application
            public void run() {
                try {
                    fileAgent = new FileAgent();
                    RMIAgentInterface IRMIAgent = new RMIAgent(NameServerInterface);
                    new RMIConnector().createRMIAgent(IRMIAgent);
                    IRMIAgent.startFileAgent(fileAgent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        controller = new Controller(false, ui);
        View viewPanel2 = new View(false);
        controller.createListeners(viewPanel2);
        viewPanel2.setVisible(true);
        fileAgent.addObserver(controller);
        ui.startUI();
    }
}

