package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnector {

    private NameServerInterface NameServerInterface;
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
            NameServerInterface = (NameServerInterface) registry.lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RMIConnector(String IP, String nodeName, int nodeCount) { //create own rmi
        try {
            INode = new Node(nodeCount, nodeName, NameServerInterface);
            String connName = nodeName+"Conn";
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

    public RMIConnector(String name) { //get node RMI
        boolean gettingConnection = true;
        while(gettingConnection) {
            try {
                Registry registry = LocateRegistry.getRegistry(NodePort);
                INodeNew = (INode) registry.lookup(name+"Conn");
                ////// !! TO DO: NameServerInterface.getHash(name) --> INode.getNewNode(hash)
                ////// INodeNew.updateNextNode etc ....
                gettingConnection = false;
            } catch (Exception e) {}
        }
        try {
            System.out.println("node RMI connected");
            System.out.println("new node hash: " + INodeNew.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NameServerInterface getNameServer() {
        return NameServerInterface;
    }
    public INode getINode() {
        return INode;
    }
}