package be.ua;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int mPrevious;
    private volatile int mNext;
    private volatile int mId;
    private volatile NameServerInterface INameServer;
    private volatile TreeMap<Integer, INode> nodeMap;
    private volatile FileMap Filemap;


    protected Node(int hash, TreeMap otherNodes, NameServerInterface ns) throws RemoteException
    {
        super();
        mId = mPrevious = mNext = hash;
        nodeMap = otherNodes;
        INameServer = ns;
    }

    public void updateNeighbours(int newPrevious, int newNext)
    {
        this.mNext = newNext;
        this.mPrevious = newPrevious;
    }

    public void updateNextNode(int newNext) {
        this.mNext = newNext;
    }

    public void updatePrevNode(int newPrev) {
        this.mPrevious = newPrev;
    }

    public int getId(){
        return mId;
    }

    public void shutdown() throws RemoteException{
        INode previousNode = nodeMap.get(mPrevious);
        previousNode.updateNextNode(mNext);

        INode nextNode = nodeMap.get(mNext);
        nextNode.updatePrevNode(mPrevious);

        Replication replication = new Replication(INameServer);
        replication.toPrevNode(this.mPrevious);

        INameServer.deleteNode(mId);
        System.exit(0);
    }

    public void addNodeToMap(int hash, INode node){
        this.nodeMap.put(hash, node);
    }

    private void setupRMI(String nodeName, int nodeCount) throws NotBoundException {
        RMIConnector connectorNode = new RMIConnector(INameServer, nodeName, nodeCount);
    }

}
