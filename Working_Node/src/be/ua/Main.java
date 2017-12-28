package be.ua;

import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static INode INode;
    static FileAgent FileAgent;
    public static TreeMap<Integer, INode> nodeMap;
    public static Controller controller;

    public static void main(String[] args) {
        nodeMap = new TreeMap<>();
        //RMIConnector connector = new RMIConnector();
        //NameServerInterface NameServerInterface = connector.getNameServer();

        //UserInterface ui = new UserInterface(NameServerInterface);
        /*
        System.out.println("\t Enter the name for the new node");
        System.out.print("> ");
        String nodeName = new Scanner(System.in).next();
        try{
            int hash = NameServerInterface.getHashOfName(nodeName);
            INode = new Node(hash, NameServerInterface);
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
        */
        FileAgent fileAgent = new FileAgent();
        controller = new Controller(fileAgent);
        View view = new View();
        controller.createListeners(view);
        view.setVisible(true);
        fileAgent.addObserver(controller);


        TreeMap<String, Boolean> map = new TreeMap<>();
        map.put("observe.txt", true);
        //fileAgent.setFileList(map);

        try{
            Thread.sleep(1000);
        }catch (Exception e){e.printStackTrace();}


        for (int i = 0; i < 10; i++){
            map.put("newtext.txt"+i, true);
        }
        //map.put("newtext.txt", true);

        fileAgent.setFileList(map);
        //ui.startUI();
    }
}

