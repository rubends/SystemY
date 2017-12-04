package be.ua;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeMap;

public class FailureAgent implements Runnable, Serializable {
    public int failureNode;
    public int agentNode;
    NameServerInterface INameServer;
    TreeMap<String, Boolean> fileList;

    public FailureAgent(int failureN, int agentN, NameServerInterface nameServerInterface) {
        failureNode = failureN;
        agentNode = agentN;
        INameServer = nameServerInterface;
    }

    @Override
    public void run() {
         fileList = UserInterface.fileList;

        Iterator<String> keySetIterator = fileList.keySet().iterator();
        while (keySetIterator.hasNext()) {
            String name = keySetIterator.next();
            try {
                String fileIP = INameServer.getFileIp(name);
                String failureIP = INameServer.getNodeIp(failureNode);
                if(fileIP.equals(failureIP)){
                    System.out.println(name + " is owned by " + failureNode + " at " + failureIP);
                }
            } catch (Exception e) {e.printStackTrace();}
        }
    }
}
