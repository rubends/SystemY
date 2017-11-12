package be.ua;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int mPrevious;
    private volatile int mNext;
    private volatile int mId;
    private volatile int nodeCount;
    private volatile String nodeName;
    private NameServerInterface ns;

    protected Node(int nc, int hash) throws RemoteException
    {
        super();
        nodeCount = nc;
        mId = hash;
    }
    public void nodeInit(int ID)
    {
        this.mId = ID;
        this.mNext = ID;
        this.mPrevious = ID;
        //System.out.println("mId,mNext,mPrev = " + ID);
    }
    public void updateNeighbour(int newPrevious, int newNext)
    {
        this.mNext = newNext;
        this.mPrevious = newPrevious;
    }

    public void updateNextNode(int newNext) {
        this.mNext = newNext;
        try {
            if(ns.getFirstId() == mId)
            {
                mPrevious = ns.getLastId();
            }
        } catch (RemoteException e) {
        e.printStackTrace();
        }
    }

    public void updatePrevNode(int newPrev) {
        this.mPrevious = newPrev;
        try {
            if(ns.getLastId() == mId)
            {
                mNext = ns.getFirstId();
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

    public int getNextNode(){
        return mNext;
    }

    public int getId(){
        return mId;
    }
    public String getName(){
        return nodeName;
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

    public void getNewNode (int hash) { }

    public void shutdown() throws RemoteException{


        RMIConnector rmi = new RMIConnector();
        //rmi.getINode(mPrevious).updateNextNode(mNext);
        //rmi.getINode(mNext).updatePrevNode(mPrevious);
        rmi.getNameServer().deleteNode(this.nodeName);
        System.exit(0);


    }
}
