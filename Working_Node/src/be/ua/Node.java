package be.ua;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int mPrevious;
    private volatile int mNext;
    private volatile int mId;
    private volatile NameServerInterface INameServer;
    private volatile Map<Integer, INode> nodeMap;



    protected Node(int hash, Map otherNodes, NameServerInterface ns) throws RemoteException
    {
        super();
        mId = hash;
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
        /*try {
            if(INameServer.getFirstId() == mId)

            {
                mPrevious = INameServer.getLastId();
            }
        } catch (RemoteException e) { e.printStackTrace(); }*/
    }

    public void updatePrevNode(int newPrev) {
        this.mPrevious = newPrev;
        /*try {
            if(INameServer.getLastId() == mId)

            {
                mNext = INameServer.getFirstId();
            }
        } catch (RemoteException e) { e.printStackTrace(); }*/
    }

    public int getId(){
        return mId;
    }

    public void shutdown() throws RemoteException{
        INode previousNode = nodeMap.get(mPrevious);
        previousNode.updateNextNode(mNext);

        INode nextNode = nodeMap.get(mNext);
        nextNode.updatePrevNode(mPrevious);

        INameServer.deleteNode(mId);
        System.exit(0);
    }

}
