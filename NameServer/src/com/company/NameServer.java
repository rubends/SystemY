package com.company;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NameServer implements NameServerInterface {
    protected NameServer() throws RemoteException {
        super();
    }

    public String getIP(String fileName) throws RemoteException {
        return fileName;
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
