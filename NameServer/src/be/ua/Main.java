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
            NameServerInterface ns = new NameServer();
            MulticastThread multicastThread = new MulticastThread(ns);
            multicastThread.start();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(registryName, ns);
            System.out.println("Nameserver bound");

            // TEST !!!!!!!!!!!!!!!!!!!!!!!!!!
            ns.addNode("testNode","192.168.1.1");
            ns.getNodeCount();
//            ns.addNode("secondnode","192.168.1.2");
//            ns.addNode("nodefiles","192.168.1.2");
//            ns.addNode("myfile","192.168.1.3");
//            ns.printNodeMap();
//            ns.deleteNode("secondnode");
            // TEST !!!!!!!!!!!!!!!!!!!!!!!!!!

        }
        catch (Exception e)
        {
            System.out.println("Nameserver error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
