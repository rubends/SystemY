package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnector {

    private Inode Inode;

    public RMIConnector(String serverPort) {

        try {
            String name = "nodeNames";
            Registry registry = LocateRegistry.getRegistry(1099);
            Inode = (Inode) registry.lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Inode getNameServer() {
        return Inode;
    }
}