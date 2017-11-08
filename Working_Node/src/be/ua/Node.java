package be.ua;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int PrevId;
    private volatile int NextId;
    private volatile int CurrId;
    private NameServerInterface ns;

    //constructor
    public Node(int id) throws RemoteException {
        PrevId = NextId = CurrId = id;
    }

    //setters
    public void updateNeighbours(int newPrev, int newNext) {
        NextId = newNext;
        PrevId = newPrev;
    }

    public void updateNextNode(int newNext) {
        NextId = newNext;
        try {
            if(ns.getFirstId() == CurrId)
            {
                try {
                    PrevId = ns.getLastId();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updatePrevNode(int newPrev) {
        PrevId = newPrev;
        try {
            if(ns.getLastId() == CurrId)
            {
                try {
                    PrevId = ns.getFirstId();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //getters
    public int getPrevId() {
        return PrevId;
    }
    public int getNextId(){
        return NextId;
    }
    public int getCurrId(){
        return CurrId;
    }

}
