package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnector {

    private NameServerInterface INameServer;
    private INode INode;
    private INode INodeNew;
    private int Port = 1099;
    private int NodePort = 1098;

    public RMIConnector() { //to nameserver
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "file:server.policy");
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "nodeNames";
            Registry registry = LocateRegistry.getRegistry(Port);
            INameServer = (NameServerInterface) registry.lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RMIConnector(NameServerInterface INameServer, String IP, String nodeName, int nodeCount) { //create own rmi
        try {
            int hash = INameServer.getHashOfName(nodeName);
            INode = new Node(nodeCount, hash);
            String connName = Integer.toString(hash);
            try {
                Registry registry = LocateRegistry.getRegistry(NodePort);
                registry.bind(connName, INode);
            } catch (Exception e) {
                Registry registry = LocateRegistry.createRegistry(NodePort);
                registry.bind(connName, INode);
            }
            ////// !!! TO DO: INode check nodecount en zet huidige, volgende en vorige node
            System.out.println("RMI bound");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
        }
    }

    public RMIConnector(NameServerInterface INameServer, String nodeName) { //get node RMI
        int hash = INameServer.getHashOfName(nodeName);
        String connName = Integer.toString(hash);
        boolean gettingConnection = true;
        while(gettingConnection) {
            try {
                Registry registry = LocateRegistry.getRegistry(NodePort);
                INodeNew = (INode) registry.lookup(connName);
                ////// !! TO DO: NameServerInterface.getHash(name) --> INode.getNewNode(hash)
                ////// INodeNew.updateNextNode etc ....
                gettingConnection = false;
            } catch (Exception e) {}
        }
        try {
            System.out.println("node RMI connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NameServerInterface getNameServer() {
        return INameServer;
    }
    public INode getINode() {
        return INode;
    }
}