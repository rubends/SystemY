package be.ua;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NameServerInterface extends Remote{
    public String getFileIp(String fileName) throws RemoteException;
}
