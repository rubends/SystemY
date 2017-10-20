package com.company;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NameServer extends UnicastRemoteObject{
    protected NameServer() throws RemoteException {
        super();
    }
}
