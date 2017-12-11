package be.ua;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIAgent extends UnicastRemoteObject implements RMIAgentInterface{
    public RMIAgent() throws RemoteException {
    }

    public FileAgent startFileAgent(FileAgent fileAgent) {
        Thread at = new Thread(fileAgent);
        System.out.println("RMIAGENT.java: File agent started.");
        at.start();
        while(true) {
            if (!at.isAlive()) {


                //todo : update
                return fileAgent;
            }
        }
    }

    public void passFileAgent(FileAgent fileAgent){
        int nextNodeId = 0;
    }


    public FileAgent startFailureAgent(FailureAgent failureAgent) throws RemoteException {
        return null;
    }


    public void passFailureAgent(FailureAgent failureAgent) throws RemoteException {

    }
}
