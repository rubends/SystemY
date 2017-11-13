package be.ua;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote {
    void updateNeighbour(int newPrevious, int newNext) throws RemoteException;
    void updatePrevNode(int newPrev) throws RemoteException;
    void updateNextNode(int newNext) throws RemoteException;
    int getPreviousNodeNext() throws RemoteException;
    int getPreviousNodePrev() throws RemoteException;
    int getId() throws RemoteException;

    //for testing , is local!
    void setIdLocal(int id) throws RemoteException;
    void actOnNodeCount(int hash,int nodeCount,INode INode) throws RemoteException;
}
