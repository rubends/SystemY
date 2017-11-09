package be.ua;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Node implements INode{
    private volatile int mPrevious;
    private volatile int mNext;
    private volatile int mId;

    public Node(int id) throws RemoteException {
        super();
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
