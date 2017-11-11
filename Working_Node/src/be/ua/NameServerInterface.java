package be.ua;

import java.rmi.Remote;
import java.util.ArrayList;

public interface NameServerInterface extends Remote{
    String getFileIp(String fileName);
    int getHashOfName(String name);
    int getNodeCount();
    ArrayList getNeighbourNodes(int hash);
}