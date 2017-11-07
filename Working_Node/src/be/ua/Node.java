package be.ua;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int mPrevious = 10;
    private volatile int mNext = 15;
    private volatile int mId= 20;

    public Node(int id) throws RemoteException {
        this.mId = id;
        this.mPrevious = id;
        this.mNext = id;
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

}
