package be.ua;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote {
    void updateNeighbours(int newPrevious, int newNext) throws RemoteException;
    void updatePrevNode(int newPrev) throws RemoteException;
    void updateNextNode(int newNext) throws RemoteException;
    int getId() throws RemoteException;
    void sendFileMap(String fileName)throws RemoteException;
    void shutdown() throws RemoteException;
}
