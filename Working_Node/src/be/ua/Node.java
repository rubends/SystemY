package be.ua;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int mPrevious;
    private volatile int mNext;
    private volatile int mId;
    private volatile int nodeCount;
    private volatile String nodeName;

    protected Node(int nc, int hash) throws RemoteException
    {
        super();
        nodeCount = nc;
        mId = hash;
    }

    public void updateNeighbour(int newPrevious, int newNext)
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

    public int getPreviousNode() {
        return mPrevious;
    }

    public int getNextNode(){
        return mNext;
    }

    public int getId(){
        return mId;
    }

    public void getNewNode (int hash) {

    }
}
