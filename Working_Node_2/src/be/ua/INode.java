package be.ua;

        import java.rmi.Remote;
        import java.rmi.RemoteException;

public interface INode extends  Remote{
    void updateNeighbour(int newPrevious, int newNext) throws RemoteException;
    void updatePrevNode(int newPrev) throws RemoteException;
    void updateNextNode(int newNext) throws RemoteException;
    int getPreviousNode() throws RemoteException;
}


