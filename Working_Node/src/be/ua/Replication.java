package be.ua;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Replication {
    NameServerInterface INameServer;
    String nodeName;
    String rootPath = new File("").getAbsolutePath();
    String sep = System.getProperty("file.separator");
    File localFolder = new File(rootPath + sep + "Files" + sep + "Local");
    File replicationFolder = new File(rootPath + sep + "Files" + sep + "Replication");

    public Replication(NameServerInterface ns) {
        INameServer = ns;
    }

    public void getFiles() {
        File[] localFiles = localFolder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            //@todo fotos fixen --> Ruben
            replicate(localFiles[i].getName(), localFiles[i].getAbsolutePath());
        }
    }

    public void replicate(String filename, String location){
        try {
            String ip = INameServer.getFileIp(filename);
            int ownHash = INameServer.getHashOfName(nodeName);
            String ownIp = INameServer.getNodeIp(ownHash);
            TCPSender tcpSender = new TCPSender(7896);
            if(!ip.equals(ownIp)){
                tcpSender.SendFile(ip, location);
            } else {
                ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(ownHash);
                int neighbourHash = neighbours.get(0);
                String prevIp = INameServer.getNodeIp(neighbourHash);
                tcpSender.SendFile(prevIp, location);
            }
            //@todo BESTANDSFICHE UPDATEN --> Sam
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void toNextNode(int hashNextNode){
        File[] replicatedFiles = replicationFolder.listFiles();
        try {
            String ipNextNode = INameServer.getNodeIp(hashNextNode);
            TCPSender tcpSender = new TCPSender(7896);
            for (int i = 0; i < replicatedFiles.length; i++) {
                String ipOwner = INameServer.getFileIp(replicatedFiles[i].getName());
                if(ipNextNode.equals(ipOwner)){
                    tcpSender.SendFile(ipNextNode, replicatedFiles[i].getAbsolutePath());
                    //replicatedFiles[i].delete(); nodig?
                    //@todo BESTANDSFICHE UPDATEN --> Sam
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toPrevNode(int hashPrevNode){
        File[] replicatedFiles = replicationFolder.listFiles();
        try {
            String ipPrevNode = INameServer.getNodeIp(hashPrevNode);
            TCPSender tcpSender = new TCPSender(7896);
            for (int i = 0; i < replicatedFiles.length; i++) {
                tcpSender.SendFile(ipPrevNode, replicatedFiles[i].getAbsolutePath());
            }
            //@todo If file is lokaal op prevNode, doe naar prev prev node --> Ruben
        } catch (Exception e) {
            e.printStackTrace();
        }
        File[] localFiles = localFolder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {

        }

    }

    public void setNodeName(String name){
        this.nodeName = name;
    }
}
//