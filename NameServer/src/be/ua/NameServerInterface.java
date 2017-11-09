package be.ua;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NameServerInterface extends Remote{
    String getFileIp(String fileName) throws RemoteException;
    void addNode(String nodeName, String nodeIP) throws RemoteException;
    void deleteNode(String nodeName) throws RemoteException;
    void printNodeMap() throws RemoteException;


}
