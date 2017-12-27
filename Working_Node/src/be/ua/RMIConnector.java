package be.ua;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class RMIConnector {

    private NameServerInterface INameServer;
    public INode INode;//was private
    //public RMIAgentInterface rmiAgentInterface;
    Registry registry;

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
            registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT); // NETWORK
            //registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT); // LOCAL
            registry.rebind(connName, iNode);
            System.out.println("Created own RMI server");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
        }
    }


    public RMIConnector(NameServerInterface INameServer, String newNodeName) throws RemoteException{ //get node RMI
        int hash = INameServer.getHashOfName(newNodeName);
        String connName = Integer.toString(hash);
        boolean gettingConnection = true;
        while(gettingConnection) {
            try {
                //Registry registry = LocateRegistry.getRegistry(nodePort);                     // --- LOCALHOST ---
                //INode INodeNew = (INode) registry.lookup(connName);                           // _________________

                String NodeIp = INameServer.getNodeIp(hash);                                    // ---- NETWORK ----
                INode INodeNew = (INode) Naming.lookup("//"+NodeIp+"/"+connName);         // _________________
                updateNewNode(INameServer, INodeNew, hash);
                System.out.println("New node id: " + INodeNew.getId());
                gettingConnection = false;
            } catch (Exception e) {
                e.printStackTrace(); //not printing because searching for connection
            }
        }
    }

    private void updateNewNode(NameServerInterface INameServer, INode INodeNew, int hash){
        try {
            if (INodeNew.getNextNode() == Main.INode.getId()) {
                Main.INode.updatePrevNode(INodeNew.getId());
            }
            if (INodeNew.getPrevNode() == Main.INode.getId()) {
                Main.INode.updateNextNode(INodeNew.getId());
            }
            Replication replication = new Replication(INameServer);
            replication.toNextNode(hash);
        } catch (Exception e){ e.printStackTrace(); }
    }

    public NameServerInterface getNameServer(){ return INameServer;}

    public void createRMIAgent(RMIAgentInterface rmiAgentInterface){ //TODO: work in progress

        try{
            Registry register = LocateRegistry.createRegistry(2000);
            register.rebind("RMIAgent", rmiAgentInterface);
            System.setProperty("java.security.policy", "file:server.policy");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}