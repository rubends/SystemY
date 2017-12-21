package be.ua;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote {
    void updateNeighbours(int newPrevious, int newNext) throws RemoteException;
    void updatePrevNode(int newPrev) throws RemoteException;
    void updateNextNode(int newNext) throws RemoteException;
    int getId() throws RemoteException;
    int getNextNode() throws RemoteException;
    int getPrevNode() throws RemoteException;
    void nodeShutdownFiles(int hash) throws RemoteException;
    void shutdown() throws RemoteException;
    void failure(int hash) throws RemoteException;
    boolean hasFile(String fileName) throws RemoteException;
    void sendFiche(FileMap fiche) throws RemoteException;
    void updateFiche(String fileName, int id, String ipLocation) throws RemoteException;
    void sendFile(String ip, String filename) throws RemoteException;
}
