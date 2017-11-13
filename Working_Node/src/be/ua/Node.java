package be.ua;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int mPrevious;
    private volatile int mNext;
    private volatile int mId;
    private volatile int nodeCount;
    private volatile String nodeName;
    private volatile NameServerInterface INameServer;
    private volatile Map<Integer, INode> nodeMap;

    protected Node(int nc, int hash, Map otherNodes, NameServerInterface ns) throws RemoteException
    {
        super();
        nodeCount = nc;
        mId = hash;
        nodeMap = otherNodes;
        INameServer = ns;
    }
    public void nodeInit(int ID)
    {
        this.mId = ID;
        this.mNext = ID;
        this.mPrevious = ID;
        //System.out.println("mId,mNext,mPrev = " + ID);
    }
    public void updateNeighbours(int newPrevious, int newNext)
    {
        this.mNext = newNext;
        this.mPrevious = newPrevious;
    }

    public void updateNextNode(int newNext) {
        this.mNext = newNext;
        try {
            if(INameServer.getFirstId() == mId)
            {
                mPrevious = INameServer.getLastId();
            }
        } catch (RemoteException e) {
        e.printStackTrace();
        }
    }

    public void updatePrevNode(int newPrev) {
        this.mPrevious = newPrev;
        try {
            if(INameServer.getLastId() == mId)
            {
                mNext = INameServer.getFirstId();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public int getPreviousNodeNext() {
        return mNext;
    }
    public int getPreviousNodePrev() {
        return mPrevious;
    }

    public int getId(){
        return mId;
    }

    public void setPreviousNodeLocal(int prevNode) {
        this.mPrevious=prevNode;
        System.out.println("prevNode on node = " + mPrevious + "="+ prevNode);
    }
    public void setNextNodeLocal(int nextNode){
        this.mNext=nextNode;
        System.out.println("nextNode on node = " + mNext);

    }
    public void setIdLocal(int id){
         this.mId=id;
        System.out.println("ID of node = " + mId);
    }

    public void shutdown() throws RemoteException{
        INode previousNode = nodeMap.get(mPrevious);
        previousNode.updateNextNode(mNext);

        INode nextNode = nodeMap.get(mNext);
        nextNode.updatePrevNode(mPrevious);

        INameServer.deleteNode(this.nodeName);
        System.exit(0);
    }
}
