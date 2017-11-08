package be.ua;

        import java.rmi.Remote;
        import java.rmi.RemoteException;

public interface INode extends  Remote{
    void updateNeighbours(int newPrev, int newNext) throws RemoteException;
    void updatePrevNode(int newPrev) throws RemoteException;
    void updateNextNode(int newNext) throws RemoteException;
    int getPrevId() throws RemoteException;
    int getNextId() throws RemoteException;
    int getCurrId() throws RemoteException;
}


