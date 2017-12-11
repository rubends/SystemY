package be.ua;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIAgentInterface extends Remote {
    FileAgent startFileAgent(FileAgent fileAgent) throws RemoteException;
    void passFileAgent(FileAgent fileAgent) throws RemoteException;
    FileAgent startFailureAgent(FailureAgent failureAgent) throws RemoteException;
    void passFailureAgent(FailureAgent failureAgent) throws RemoteException;
}
