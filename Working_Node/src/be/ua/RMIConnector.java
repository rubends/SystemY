package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnector {

    private NameServerInterface NameServerInterface;
    private INode INode;

    public RMIConnector() { //to nameserver

        try {
            String name = "nodeNames";
            Registry registry = LocateRegistry.getRegistry(1099);
            NameServerInterface = (NameServerInterface) registry.lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RMIConnector(String IP, String name) { //create own rmi

        System.setProperty("java.rmi.server.hostname", IP);
        try {
            INode = new Node();
            Registry registry = LocateRegistry.getRegistry(1099);
            registry.bind(name, INode);
            System.out.println("RMI bound");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
        }
    }

    public RMIConnector(int serverPort, String name) { //get node RMI
        try {
            Registry registry = LocateRegistry.getRegistry(serverPort);
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