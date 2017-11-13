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
        System.out.println("next of node = " + mNext);

       /* try {
            if(ns.getFirstId() == mId)
            {
                mPrevious = ns.getLastId();
            }
        } catch (RemoteException e) {
        e.printStackTrace();
        }*/
    }

    public void updatePrevNode(int newPrev) {
        this.mPrevious = newPrev;
        System.out.println("prev of node = " + mPrevious);
    /*    try {
            if(ns.getLastId() == mId)
            {
                mNext = ns.getFirstId();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/
    }

    public int getPreviousNodeNext() {
        System.out.println("Got from new node: nextNode = " + mNext );
        return mNext;
    }
    public int getPreviousNodePrev() {
        System.out.println("Got from new node: prevNode = " + mPrevious );
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
    public void setIdLocal(int id){
        this.mId=id;
        System.out.println("ID of node = " + mId);
    }
    public void actOnNodeCount(int hash,int nodeCount,INode INode)
    {
        System.out.println("nodeCount = " + nodeCount);
        if ((nodeCount-1) <1){
            try{
                System.out.println("HERE = " + nodeCount);

                INode.updatePrevNode(hash);
                INode.updateNextNode(hash);
                INode.setIdLocal(hash);
            }
            catch(RemoteException e){
                //failure.ActOnFailure(INameServer, INode);
            }
        }
        else if((nodeCount-1) >= 1){
            try{
                int nextNode = INode.getPreviousNodeNext();
                int prevNode = INode.getPreviousNodePrev();
                System.out.println("Got from new node: nextNode = " + nextNode + " prevNode = " + prevNode);
                INode.updatePrevNode(prevNode);
                INode.updateNextNode(nextNode);
                INode.setIdLocal(hash);
            }
            catch(RemoteException e){
                //failure.ActOnFailure(INameServer, INode);
            }
        }else
        {

        }

    }
    public void getNewNode (int hash) { }
}
