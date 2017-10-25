package com.company;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {
        String registryName = "nodeNames";
        try
        {
            NameServer ns = new NameServer();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(registryName, ns);
            System.out.println("Nameserver bound");

        }
        catch (Exception e)
        {
            System.out.println("Nameserver error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
