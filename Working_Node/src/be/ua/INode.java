package be.ua;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote {
    void updateNeighbours(int newPrevious, int newNext) throws RemoteException;
    void updatePrevNode(int newPrev) throws RemoteException;
    void updateNextNode(int newNext) throws RemoteException;
    int getId() throws RemoteException;
    void nodeShutdownFiles(int hash) throws RemoteException;
    boolean hasFile(File file) throws RemoteException;


    void sendFiche(FileMap fiche) throws RemoteException;
    void updateFiche(String fileName, int id, String ipLocation) throws RemoteException;
}
