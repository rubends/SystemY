package be.ua;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIAgentInterface extends Remote {
    void startFileAgent(FileAgent fileAgent, String owner) throws RemoteException;
    void passFileAgent(FileAgent fileAgent) throws RemoteException;
    void startFailureAgent(FailureAgent failureAgent) throws RemoteException;
    void passFailureAgent(FailureAgent failureAgent) throws RemoteException;
}
