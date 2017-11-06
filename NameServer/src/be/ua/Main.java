package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {
        String registryName = "nodeNames";
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "file:src/server.policy");
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            System.setSecurityManager(new SecurityManager());
        }
        try
        {
            NameServer ns = new NameServer();
            MulticastThread multicastThread = new MulticastThread(ns);
            multicastThread.start();

            NameServerInterface nsI = new NameServer();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(registryName, nsI);
            System.out.println("Nameserver bound");

            // TEST !!!!!!!!!!!!!!!!!!!!!!!!!!
//            nsI.addNode("node1","192.168.1.1");
//            nsI.addNode("secondnode","192.168.1.2");
//            nsI.addNode("nodefiles","192.168.1.2");
//            nsI.addNode("myfile","192.168.1.3");
//            nsI.printNodeMap();
//            nsI.deleteNode("secondnode");
            // TEST !!!!!!!!!!!!!!!!!!!!!!!!!!

        }
        catch (Exception e)
        {
            System.out.println("Nameserver error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
