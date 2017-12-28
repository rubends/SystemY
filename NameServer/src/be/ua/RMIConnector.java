package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnector {

    public RMIConnector(NameServerInterface ns) {
        String registryName = "nodeNames";
        System.setProperty("java.security.policy", "file:server.policy");
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try
        {

            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.bind(registryName, ns);
            System.out.println("Nameserver bound");

            // TEST !!!!!!!!!!!!!!!!!!!!!!!!!!
            //ns.addNode("testNode","192.168.1.1"); //For test
//            ns.getNodeCount();
//            ns.addNode("secondnode","192.168.1.2");
//            ns.addNode("nodefiles","192.168.1.2");
//            ns.addNode("myfile","192.168.1.3");
//            ns.printNodeMap();
//            ns.deleteNode(hash van testNode);
            // TEST !!!!!!!!!!!!!!!!!!!!!!!!!!

        }
        catch (Exception e)
        {
            System.out.println("Nameserver error: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
