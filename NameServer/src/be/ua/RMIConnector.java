package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnector {

    private int Port = 1099;

    public RMIConnector(NameServerInterface ns) {
        String registryName = "nodeNames";
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "file:server.policy");
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            System.setSecurityManager(new SecurityManager());
        }
        try
        {
            Registry registry = LocateRegistry.createRegistry(Port);
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
