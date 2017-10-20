package com.company;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NameServerInterface extends Remote{
    public String getIP(String fileName) throws RemoteException;
}
