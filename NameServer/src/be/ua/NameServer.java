package be.ua;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;

public class NameServer implements NameServerInterface {
    protected NameServer() throws RemoteException {
        super();
    }

    public String getFileIp(String fileName) throws RemoteException {
        int hash = getHashOfName(fileName);
        int mapKey = 0;

        Iterator keys = nodeMap.entrySet().iterator();
        if(hash < nodeMap.firstKey()){
            mapKey = nodeMap.lastKey();
        }

        while (keys.hasNext()) {
            int key = keys.next();
            if(key < hash || key == hash)
            {
                mapKey = key;
            }
        }
        System.out.println("IP of node: " + nodeMap.get(mapKey) + "\n");
        return nodeMap.get(mapKey);
    }

    public static int getHashOfName(String name) {
        return Math.abs(name.hashCode() % 32769);
    }

    public static void main(String[] args) {
        String registryName = "nodeNames";
        try
        {
            NameServerInterface ns = new NameServer();
            NameServerInterface stub = (NameServerInterface) UnicastRemoteObject.exportObject(ns, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(registryName, stub);
            System.out.println("Nameserver bound");

        }
        catch (Exception e)
        {
            System.out.println("Nameserver error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
