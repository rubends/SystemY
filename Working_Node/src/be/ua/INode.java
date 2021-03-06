package be.ua;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.TreeMap;

public interface INode extends Remote {
    void updateNeighbours(int newPrevious, int newNext) throws RemoteException;
    void updatePrevNode(int newPrev) throws RemoteException;
    void updateNextNode(int newNext) throws RemoteException;
    int getId() throws RemoteException;
    int getNextNode() throws RemoteException;
    int getPrevNode() throws RemoteException;
    void shutdown() throws RemoteException;
    void failure(int hash) throws RemoteException;
    boolean hasFile(String fileName) throws RemoteException;
    void sendFiche(FileMap fiche) throws RemoteException;
    void updateFiche(String fileName, int id, String ipLocation) throws RemoteException;
    void sendFile(String ip, String filename) throws RemoteException;
    void deleteFile(String fileName) throws  RemoteException;
    String getDownloadLocation(String filename) throws RemoteException;
    void deleteFileLocation(String filename, int nodeHash) throws RemoteException;
    void deleteLocalFile(String filename) throws RemoteException;
    void setLocalFileList( TreeMap<String, Boolean> fileList) throws RemoteException;
    TreeMap<String, Boolean> getLocalFileList() throws RemoteException;
}
