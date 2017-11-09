package be.ua;

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
    public RMIConnector(int serverPort,String name) {

        try {
            System.out.print("> TRYING TO GET CONNECTION ON PORT: "+serverPort);

            Registry registry = LocateRegistry.getRegistry(serverPort);//moet serverport zijn
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