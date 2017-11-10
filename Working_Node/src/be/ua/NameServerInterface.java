package be.ua;

import java.rmi.Remote;

public interface NameServerInterface extends Remote{
    String getFileIp(String fileName);
    int getHashOfName(String name);
    int getNodeCount();
}