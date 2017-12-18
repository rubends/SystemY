package be.ua;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class RMIConnector {

    private NameServerInterface INameServer;
    public INode INode;//was private

    public RMIConnector() { //to nameserver
        try {
            String name = "nodeNames";
            INameServer = (NameServerInterface) Naming.lookup("//"+Main.ipNameServer+"/"+name);
        } catch (Exception e) {
            System.out.println("No nameserver found.");
            e.printStackTrace();
        }
    }

    public RMIConnector(NameServerInterface INameServer, String nodeName, INode iNode) { //create own rmi
        try {
            int hash = INameServer.getHashOfName(nodeName);
            String connName = Integer.toString(hash);
            System.setProperty("java.security.policy", "file:server.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind(connName, iNode);
            System.out.println("Created own RMI server");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
        }
    }


    public RMIConnector(NameServerInterface INameServer, String newNodeName, String nodeName) throws RemoteException{ //get node RMI
        int hash = INameServer.getHashOfName(newNodeName);
        String connName = Integer.toString(hash);
        boolean gettingConnection = true;
        while(gettingConnection) {
            try {
                //Registry registry = LocateRegistry.getRegistry(nodePort);                     // --- LOCALHOST ---
                //INode INodeNew = (INode) registry.lookup(connName);                           // _________________

                String NodeIp = INameServer.getNodeIp(hash);                                    // ---- NETWORK ----
                INode INodeNew = (INode) Naming.lookup("//"+NodeIp+"/"+connName);         // _________________
                ArrayList<Integer> ids = INameServer.getNeighbourNodes(hash);
                INodeNew.updateNeighbours(ids.get(0), ids.get(1));
                System.out.println("New node id: " + INodeNew.getId());
                if(ids.get(0) == INameServer.getHashOfName(nodeName)){
                    Replication replication = new Replication(INameServer);
                    replication.toNextNode(hash);
                }
                gettingConnection = false;
            } catch (Exception e) {
                e.printStackTrace(); //not printing because searching for connection
            }
        }
    }

    public NameServerInterface getNameServer(){ return INameServer;}
}