package be.ua;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.TreeMap;

public class RMIConnector {

    private NameServerInterface INameServer;
    private INode INode;
    private INode INodeNew;

    private int Port = 1099;
    private int NodePort = 1098;
    private TreeMap<Integer, INode> nodeMap = new TreeMap<>();

    public RMIConnector() { //to nameserver
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "file:server.policy");
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            //System.setProperty("java.rmi.server.hostname", "169.254.62.119");
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "nodeNames";
            Registry registry = LocateRegistry.getRegistry(Port);
            INameServer = (NameServerInterface) registry.lookup(name);
            //INameServer = Naming.lookup("//169.254.62.119/nodeNames");
        } catch (Exception e) {
            System.out.println("No nameserver found.");
            e.printStackTrace();
        }
    }

    public RMIConnector(NameServerInterface INameServer, String nodeName, int nodeCount) { //create own rmi
        try {
            int hash = INameServer.getHashOfName(nodeName);
            INode = new Node(hash, nodeMap, INameServer);
            String connName = Integer.toString(hash);
            try {
                Registry registry = LocateRegistry.getRegistry(NodePort);
                registry.bind(connName, INode);
            } catch (Exception e) {
                Registry registry = LocateRegistry.createRegistry(NodePort);
                registry.bind(connName, INode);
            }
            System.out.println("serverRMI bound to server");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
        }
    }


    public RMIConnector(NameServerInterface INameServer, String newNodeName, String nodeName) throws RemoteException{ //get node RMI
        int hash = INameServer.getHashOfName(newNodeName);
        String connName = Integer.toString(hash);
        boolean gettingConnection = true;
        while(gettingConnection) {
            try {
                Registry registry = LocateRegistry.getRegistry(NodePort);
                INodeNew = (INode) registry.lookup(connName);
                nodeMap.put(hash, INodeNew);
                ArrayList<Integer> ids = INameServer.getNeighbourNodes(hash);
                INodeNew.updateNeighbours(ids.get(0), ids.get(1));
                if(ids.get(0) == INameServer.getHashOfName(nodeName)){
                    Replication replication = new Replication(nodeName, INameServer);
                    replication.getFiles();
                }
                gettingConnection = false;
            } catch (Exception e) {}
        }
    }

    public void nodeFailure(NameServerInterface INameServer, int hash)
    {
        try {
            ArrayList<Integer> failbourNodes = INameServer.getNeighbourNodes(hash);
            INameServer.deleteNode(hash);
            INode prevNode = nodeMap.get(failbourNodes.get(0));
            prevNode.updateNextNode(failbourNodes.get(0));
            INode nextNode = nodeMap.get(failbourNodes.get(1));
            nextNode.updatePrevNode(failbourNodes.get(2));
        } catch (Exception e) { e.printStackTrace(); }
    }
    public NameServerInterface getNameServer(){ return INameServer;}
}