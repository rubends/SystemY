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

        System.out.println("next of node = " + mNext);

       /* try {
            if(ns.getFirstId() == mId)

        try {
            if(INameServer.getFirstId() == mId)

            {
                mPrevious = INameServer.getLastId();
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

        try {
            if(INameServer.getLastId() == mId)

            {
                mNext = INameServer.getFirstId();
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


    public void shutdown() throws RemoteException{
        INode previousNode = nodeMap.get(mPrevious);
        previousNode.updateNextNode(mNext);

        INode nextNode = nodeMap.get(mNext);
        nextNode.updatePrevNode(mPrevious);

        INameServer.deleteNode(this.nodeName);
        System.exit(0);
    }

}
