package be.ua;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;

public class RMIConnector {

    private NameServerInterface INameServer;
    public INode INode;//was private
    //public static INode INodeNew;//was private

    private int NodePort = 1098;
    private String ipNameServer = "127.0.0.1"; //127.0.0.1

    public RMIConnector() { //to nameserver
        try {
            String name = "nodeNames";
            INameServer = (NameServerInterface) Naming.lookup("//"+ipNameServer+"/"+name);
        } catch (Exception e) {
            System.out.println("No nameserver found.");
            e.printStackTrace();
        }
    }

    public RMIConnector(NameServerInterface INameServer, String nodeName, int nodeCount) { //create own rmi
        try {
            int hash = INameServer.getHashOfName(nodeName);
            INode = Main.INode;
            String connName = Integer.toString(hash);
            if (System.getSecurityManager() == null) {
                System.setProperty("java.security.policy", "file:server.policy");
                System.setSecurityManager(new SecurityManager());
            }
            try {
                Registry registry = LocateRegistry.getRegistry(NodePort);
                registry.rebind(connName, INode);
            } catch (Exception e) {
                Registry registry = LocateRegistry.createRegistry(NodePort);
                registry.rebind(connName, INode);
            }
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
                Registry registry = LocateRegistry.getRegistry(NodePort);               // --- LOCALHOST ---
                INode INodeNew = (INode) registry.lookup(connName);                     // _________________

                //String NodeIp = INameServer.getNodeIp(hash);                          // ---- NETWORK ----
                //INode INodeNew = (INode) Naming.lookup("//"+NodeIp+"/"+connName);     // _________________

                Main.nodeMap.put(hash, INodeNew);
                INodeNew.addNodeToMap(Main.INode.getId(), Main.INode);
                Main.INode.addNodeToMap(hash, INodeNew);
                ArrayList<Integer> ids = INameServer.getNeighbourNodes(hash);
                INodeNew.updateNeighbours(ids.get(0), ids.get(1));
                System.out.println("New node id: " + INodeNew.getId());
                if(ids.get(0) == INameServer.getHashOfName(nodeName)){
                    Replication replication = new Replication(INameServer);
                    replication.toNextNode(hash);
                }
                gettingConnection = false;
            } catch (Exception e) {
                // e.printStackTrace(); //not printing because searching for connection
            }
        }
    }

    public void nodeFailure(NameServerInterface INameServer, int hash) //BIJ ELKE NODE EXCEPTION
    { //@todo OPROEPEN BIJ EXCEPTIONS
        try {
            ArrayList<Integer> failbourNodes = INameServer.getNeighbourNodes(hash);
            INameServer.deleteNode(hash);
            INode prevNode = Main.nodeMap.get(failbourNodes.get(0));
            prevNode.updateNextNode(failbourNodes.get(0));
            INode nextNode = Main.nodeMap.get(failbourNodes.get(1));
            nextNode.updatePrevNode(failbourNodes.get(2));
        } catch (Exception e) { e.printStackTrace(); }
    }
    public NameServerInterface getNameServer(){ return INameServer;}
}