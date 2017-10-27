package SP;

import java.rmi.Remote;

public interface Inode extends Remote{
    String getFileIp(String args);
}
