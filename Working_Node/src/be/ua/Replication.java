package be.ua;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Replication {
    NameServerInterface INameServer;
    String nodeName;
    String rootPath = new File("").getAbsolutePath();
    String sep = System.getProperty("file.separator");
    File[] localFiles;
    File[] replicatedFiles;

    public Replication(String name, NameServerInterface ns) {
        nodeName = name;
        INameServer = ns;
    }

    public void getFiles() {
        File folder = new File(rootPath + sep + "Files" + sep + "Local");
        localFiles = folder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            //!!!!!!!!!!!!!!!!!!!!! fotos werken nog niet
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
                System.out.println(neighbourHash);
                String prevIp = INameServer.getNodeIp(neighbourHash);
                tcpSender.SendFile(prevIp, location);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void prevNode(int hashNextNode){
        File folder = new File(rootPath + sep + "Files" + sep + "Replication");
        replicatedFiles = folder.listFiles();
        for (int i = 0; i < replicatedFiles.length; i++) {
            try{
                String ipOwner = INameServer.getFileIp(replicatedFiles[i].getName());
                String ipNextNode = INameServer.getNodeIp(hashNextNode);
                if(ipNextNode.equals(ipOwner)){
                    TCPSender tcpSender = new TCPSender(7896);
                    tcpSender.SendFile(ipNextNode, replicatedFiles[i].getName());
                    //Dan ook nog de bestandsfiche invoegen!!
                }
            }
            catch(IOException e){ }
        }
    }
}
