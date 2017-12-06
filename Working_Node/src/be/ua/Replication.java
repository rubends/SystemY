package be.ua;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.util.ArrayList;

public class Replication {
    NameServerInterface INameServer;
    String nodeName;
    String rootPath = new File("").getAbsolutePath();
    String sep = System.getProperty("file.separator");
    File localFolder = new File(rootPath + sep + "Files" + sep + "Local");
    File replicationFolder = new File(rootPath + sep + "Files" + sep + "Replication");
    private int SOCKET_PORT = 7897;


    public Replication(NameServerInterface ns) {
        INameServer = ns;
    }

    public void getFiles() {
        File[] localFiles = localFolder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            replicate(localFiles[i].getName(), localFiles[i].getAbsolutePath());
        }
    }

    public void replicate(String filename, String location){
        try {
            String ip = INameServer.getFileIp(filename);
            int ownHash = INameServer.getHashOfName(nodeName);
            String ownIp = INameServer.getNodeIp(ownHash);
            TCPSender tcpSender = new TCPSender(SOCKET_PORT);
            if(!ip.equals(ownIp)){
                tcpSender.SendFile(ip, location);
            } else {
                ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(ownHash);
                int neighbourHash = neighbours.get(0);
                System.out.println("neighbourHash = "+ neighbourHash);
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
            TCPSender tcpSender = new TCPSender(SOCKET_PORT);
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
            TCPSender tcpSender = new TCPSender(SOCKET_PORT);
            for (int i = 0; i < replicatedFiles.length; i++) {
                if(INameServer.getFileIp(replicatedFiles[i].getName()).equals(ipPrevNode)){
                    ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(hashPrevNode);
                    String ipPrevPrevNode = INameServer.getNodeIp(neighbours.get(0));
                    tcpSender.SendFile(ipPrevPrevNode, replicatedFiles[i].getAbsolutePath());
                } else {
                    tcpSender.SendFile(ipPrevNode, replicatedFiles[i].getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File[] localFiles = localFolder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            String nodeIp = "123"; //@todo GET ALLE OWNER IP'S VAN FILE UIT BESTANDSFICHE --> Sam
            int nodeHash = 3456; //@todo GET ALLE OWNER HASHED VAN FILE UIT BESTANDSFICHE --> Sam
            //FOR LOOP
            try {
                INode nodeRMI = (INode) Naming.lookup("//"+nodeIp+"/"+nodeHash);
                int fileHash = INameServer.getHashOfName(localFiles[i].getName());
                nodeRMI.nodeShutdownFiles(fileHash);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void setNodeName(String name){
        this.nodeName = name;
    }
}
//