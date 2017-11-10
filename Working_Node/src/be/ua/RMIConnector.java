package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnector {

    private NameServerInterface NameServerInterface;
    private INode INode;
    private int Port = 1099;

    public RMIConnector() { //to nameserver

        try {
            String name = "nodeNames";
            Registry registry = LocateRegistry.getRegistry(Port);
            NameServerInterface = (NameServerInterface) registry.lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RMIConnector(String IP, String name) { //create own rmi

        System.setProperty("java.rmi.server.hostname", IP);
        try {
            INode = new Node();
            Registry registry = LocateRegistry.getRegistry(Port);
            registry.bind(name, INode);
            System.out.println("RMI bound");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
        }
    }

    public RMIConnector(String name) { //get node RMI
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