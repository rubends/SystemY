package be.ua;

import java.rmi.Remote;

public interface Inode extends Remote{
    String getFileIp(String fileName);
}