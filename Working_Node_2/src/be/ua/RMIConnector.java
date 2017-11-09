package be.ua;

import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;



public class RMIConnector {
    private NameServerInterface NameServerInterface;
    public INode INode;

    public RMIConnector(String serverPort) {
        try {
            String name = "nodeNames";
            Registry registry = LocateRegistry.getRegistry(1099);
            NameServerInterface = (NameServerInterface) registry.lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public RMIConnector(String IP,String name, int nodeId) {

            System.setProperty("java.rmi.server.hostname", IP);
            try {
                String serverName = name;
                INode node = new Node(nodeId);
                INode stub = (INode) UnicastRemoteObject.exportObject(node, 0);
                Registry registry = LocateRegistry.createRegistry(1099);
                registry.rebind(serverName, stub);
                System.out.println("RMI bound");
            } catch (Exception e) {
                System.err.println("Exception while setting up RMI:");
                e.printStackTrace();
            }
    }



    public NameServerInterface getNameServer() { return NameServerInterface; }
    public INode getINode() {
        return INode;
    }

}