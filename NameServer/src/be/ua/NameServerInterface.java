package be.ua;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface NameServerInterface extends Remote{
    String getFileIp(String fileName) throws RemoteException;
    String getNodeIp(int hash) throws RemoteException;
    int getHashOfIp(String IP)throws RemoteException;
    void addNode(String nodeName, String nodeIP) throws RemoteException;
    void deleteNode(int hash) throws RemoteException;
    void printNodeMap() throws RemoteException;
    void loadTreeMap() throws RemoteException;
    int getNodeCount() throws RemoteException;
    int getHashOfName(String name) throws RemoteException;
    ArrayList getNeighbourNodes(int hash) throws RemoteException;
    int getLastId() throws RemoteException;
    int getFirstId() throws RemoteException;
}
