package be.ua;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    //global variables
    public static String ipNameServer = "169.254.91.69"; //127.0.0.1
    public static INode INode;
    public static String rootPath = new File("").getAbsolutePath();
    public static String sep = System.getProperty("file.separator"); //OS dependable
    public static String pathToFiles = rootPath + sep + "Files" + sep;
    public static String pathToLocalFiles = rootPath + sep + "Files" + sep + "Local" + sep;
    public static String pathToReplFiles = rootPath + sep + "Files" + sep + "Replication" + sep;


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


        FileAgent fileAgent = new FileAgent();
        Controller controller = new Controller(fileAgent);
        View view = new View();
        controller.createListeners(view);
        view.setVisible(true);
        fileAgent.addObserver(controller);


        //implementation of agents
        (new Thread(){ // keep going without interrupting application
            public void run() {
                try {
                    FileAgent fileAgent = new FileAgent();
                    RMIAgentInterface IRMIAgent = new RMIAgent(NameServerInterface);
                    new RMIConnector().createRMIAgent(IRMIAgent);
                    IRMIAgent.startFileAgent(fileAgent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //ui.startUI();
    }
}

