package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnector {

    private NameServerInterface NameServerInterface;
    private INode INode;
    private int Port = 1099;

    public RMIConnector() { //to nameserver
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "file:src/server.policy");
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

        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "file:src/server.policy");
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            System.setSecurityManager(new SecurityManager());
        }
        try {
            INode = new Node(nodeCount);
            Registry registry = LocateRegistry.getRegistry(Port);
            String connName = nodeName+"Conn";
            registry.bind(connName, INode);
            System.out.println("RMI bound");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
        }
    }

    public RMIConnector(String name) { //get node RMI
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy","file:src/client.policy");
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry(Port);
            INode = (INode) registry.lookup(name);
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