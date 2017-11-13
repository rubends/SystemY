package be.ua;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RMIConnector {

    private NameServerInterface INameServer;
    private INode INode;
    private INode INodeNew;

    private int Port = 1099;
    private int NodePort = 1098;
    Map<Integer, INode> nodeMap = new HashMap<>();

    private Failure failure;

    public RMIConnector() { //to nameserver
        this.failure = new Failure(INameServer,INode);
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "file:server.policy");
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "nodeNames";
            Registry registry = LocateRegistry.getRegistry(Port);
            INameServer = (NameServerInterface) registry.lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
            //failure.ActOnFailure(INameServer, INode);
        }
    }

    public RMIConnector(NameServerInterface INameServer, String IP, String nodeName, int nodeCount) { //create own rmi
        this.failure = new Failure(INameServer,INode);
        try {
            int hash = INameServer.getHashOfName(nodeName);
            INode = new Node(nodeCount, hash, nodeMap, INameServer);
            String connName = Integer.toString(hash);
            try {
                Registry registry = LocateRegistry.getRegistry(NodePort);
                registry.bind(connName, INode);
            } catch (Exception e) {
                Registry registry = LocateRegistry.createRegistry(NodePort);
                registry.bind(connName, INode);
                //failure.ActOnFailure(INameServer, INode);
            }
            ////// !!! TO DO: INode check nodecount en zet huidige, volgende en vorige node
//-----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
            INode.actOnNodeCount(hash,nodeCount,INode);

            System.out.println("serverRMI bound to server");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
            //failure.ActOnFailure(INameServer, INode);
        }
//-----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
    }


    public RMIConnector(NameServerInterface INameServer, String nodeName) throws RemoteException{ //get node RMI
        this.failure = new Failure(INameServer,INode);
        int hash = INameServer.getHashOfName(nodeName);
        String connName = Integer.toString(hash);
        boolean gettingConnection = true;
        while(gettingConnection) {
            try {
                Registry registry = LocateRegistry.getRegistry(NodePort);
                INodeNew = (INode) registry.lookup(connName);

                nodeMap.put(hash, INodeNew);
                ArrayList<Integer> ids = INameServer.getNeighbourNodes(hash);
                INodeNew.updateNeighbours(ids.get(0), ids.get(1));
                gettingConnection = false;
            } catch (Exception e) {//failure.ActOnFailure(INameServer, INode);
                }

        }
    }

    public NameServerInterface getNameServer() {
        return INameServer;
    }
    public INode getINode() {
        return INode;
    }

    public Map getNodeMap(){
        return nodeMap;
    }

    public Failure getFailure()
    {
        return this.failure;
    }
}