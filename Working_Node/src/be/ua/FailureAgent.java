package be.ua;

import java.io.File;
import java.io.Serializable;
import java.rmi.Naming;
import java.util.Iterator;
import java.util.TreeMap;

public class FailureAgent implements Runnable, Serializable {
    public int failureNode;
    public int agentNode;
    NameServerInterface INameServer;
    TreeMap<File, Boolean> fileList;
    private int SOCKET_PORT = 7896;

    public FailureAgent(int failureN, int agentN, NameServerInterface nameServerInterface) {
        failureNode = failureN;
        agentNode = agentN;
        INameServer = nameServerInterface;
    }

    @Override
    public void run() {
         fileList = Node.fileList;

        Iterator<File> keySetIterator = fileList.keySet().iterator();
        while (keySetIterator.hasNext()) {
            File file = keySetIterator.next();
            String name = file.getName();
            try {
                String fileIP = INameServer.getFileIp(name);
                int fileHash = INameServer.getHashOfName(name);
                String failureIP = INameServer.getNodeIp(failureNode);
                if(fileIP.equals(failureIP)){
                    System.out.println(name + " is owned by " + failureNode + " at " + failureIP);
                    int newOwner = INameServer.getNeighbourNodes(failureNode).get(0);
                    String newOwnerIP = INameServer.getNodeIp(newOwner);
                    INode newOwnerNode = (INode) Naming.lookup("//" + newOwnerIP + "/" + newOwner);
                    if(!newOwnerNode.hasFile(file)){
                        TCPSender tcpSender = new TCPSender(SOCKET_PORT);
                        tcpSender.SendFile(newOwnerIP, file.getAbsolutePath());
                    }
                    newOwnerNode.updateFiche(name, fileHash, newOwnerIP); //@todo JUIST IP?
                }
            } catch (Exception e) {e.printStackTrace();}
        }
    }
}
