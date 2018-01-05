package be.ua;

import java.io.File;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class Replication {
    private NameServerInterface INameServer;
    private String nodeName;
    private File localFolder = new File(Main.pathToLocalFiles);
    private File replicationFolder = new File(Main.pathToReplFiles);
    private int SOCKET_PORT = 7897;//7897

    public static volatile TreeMap<String, FileMap> fileMap;

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

            int hash = INameServer.getHashOfIp(ip);

            if(!ip.equals(ownIp)){
                //maak fiche voor nieuwe owner
                FileMap fiche = fileMap.get(filename);
                fiche.addLocation(ip, hash);

                //nu fiche lokaal verwijderen ->is geen owner meer
                String nodeIp = INameServer.getNodeIp(hash);
                INode INodeNew = (INode) Naming.lookup("//"+nodeIp+"/"+Integer.toString(hash));
                System.out.println("REPLICATION file " + filename + " replicated to " + nodeIp);
                INodeNew.sendFiche(fiche);
                fileMap.remove(filename); // dont remove local file fiches

                tcpSender.SendFile(ip, location);
            } else {
                //ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(ownHash);
                int neighbourHash = Main.INode.getPrevNode();
                if(ownHash != neighbourHash){
                    String prevIp = INameServer.getNodeIp(neighbourHash);
                    System.out.println("REPLICATION file " + filename + " replicated to previous node " + prevIp);
                    FileMap fiche = fileMap.get(filename);
                    //INode INodePrev = (INode) Naming.lookup("//"+prevIp+"/"+neighbourHash); // DELETED LINE BECAUSE LOCAL FILES BELONG TO OWNER
                    //fiche.addLocation(ownIp, ownHash);
                    //fileMap.put(filename, fiche);
                    //INodePrev.sendFiche(fiche);
                    //fileMap.remove(filename);
                    fiche.addLocation(prevIp, neighbourHash);
                    tcpSender.SendFile(prevIp, location);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void toNextNode(int hashNextNode){ //Nieuwe node komt erbij
        File[] replicatedFiles = replicationFolder.listFiles();
        File[] localFiles = localFolder.listFiles();
        try {
            String ipNextNode = INameServer.getNodeIp(hashNextNode);
            String ownIp = INameServer.getNodeIp(Main.INode.getId());
            String prevIp = INameServer.getNodeIp(Main.INode.getPrevNode());
            int prevPrevHash = INameServer.getNeighbourNodes(Main.INode.getPrevNode()).get(0);
            TCPSender tcpSender = new TCPSender(SOCKET_PORT);
            for (int i = 0; i < replicatedFiles.length; i++) {
                String ipOwner = INameServer.getFileIp(replicatedFiles[i].getName());
                if(ipNextNode.equals(ipOwner)){
                    INode NextNode = (INode) Naming.lookup("//"+ipNextNode+"/"+Integer.toString(hashNextNode));
                    if(!NextNode.hasFile(replicatedFiles[i].getName())) { //if next node doesnt have the file already
                        System.out.println("REPLICATION sending replicated file: " + replicatedFiles[i].getName() + " to " + ipNextNode + " if it equals " + ipOwner);
                        tcpSender.SendFile(ipNextNode, replicatedFiles[i].getAbsolutePath());
                        if (fileMap.containsKey(replicatedFiles[i].getName())) {
                            passFiche(replicatedFiles[i].getName(), ipNextNode);
                            //FICHE DOORSTUREN + TOEVOEGEN AAN LIJST
                        }
                    }
                } else if(hashNextNode == prevPrevHash && ipOwner.equals(prevIp)) { //if new node is prev node from prev node fix replicated files belonging to prev prev node
                    String prevPrevIp = INameServer.getNodeIp(prevPrevHash);
                    INode newNode = (INode) Naming.lookup("//"+prevPrevIp+"/"+prevPrevHash);
                    if(!newNode.hasFile(replicatedFiles[i].getName())) {
                        System.out.println("REPLICATION sending replicated file: " + replicatedFiles[i].getName() + " to rightful owner " + prevPrevIp);
                        tcpSender.SendFile(ipNextNode, replicatedFiles[i].getAbsolutePath());
                        if (fileMap.containsKey(replicatedFiles[i].getName())) {
                            passFiche(replicatedFiles[i].getName(), ipNextNode);
                        }
                    }
                }
            }
            if(Main.INode.getPrevNode() != Main.INode.getId() && Main.INode.getPrevNode() == Main.INode.getNextNode()) { // WEL LOCAL FILES CHECKEN WANNEER ER 1 BUUR IS.
                for (int i = 0; i < localFiles.length; i++) {
                    String ipOwner = INameServer.getFileIp(localFiles[i].getName());
                    if (ipNextNode.equals(ipOwner)) {
                        System.out.println("REPLICATION sending local file: " + localFiles[i].getName() + " to " + ipNextNode + " if it equals " + ipOwner);
                        tcpSender.SendFile(ipNextNode, localFiles[i].getAbsolutePath());
                        if (fileMap.containsKey(localFiles[i].getName())) {
                            passFiche(localFiles[i].getName(), ipNextNode); //FICHE DOORSTUREN + TOEVOEGEN AAN LIJST
                        }
                    } else if (ownIp.equals(ipOwner)) { // !!!! WHEN LOCAL FILES ARE BELONGING TO OWN NODE, THEY ARE REPLICATED TO NEW NODE
                        System.out.println("REPLICATION sending local file: " + localFiles[i].getName() + " to " + prevIp);
                        tcpSender.SendFile(prevIp, localFiles[i].getAbsolutePath());
                        fileMap.get(localFiles[i].getName()).addLocation(prevIp, Main.INode.getPrevNode());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toPrevNode(int hashPrevNode){ //shutdown
        File[] replicatedFiles = replicationFolder.listFiles();
        try {
            String ipPrevNode = INameServer.getNodeIp(hashPrevNode);
            TCPSender tcpSender = new TCPSender(SOCKET_PORT);
            for (int i = 0; i < replicatedFiles.length; i++) {
                if(INameServer.getFileIp(replicatedFiles[i].getName()).equals(ipPrevNode)){
                    ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(hashPrevNode);
                    String ipPrevPrevNode = INameServer.getNodeIp(neighbours.get(0));
                    tcpSender.SendFile(ipPrevPrevNode, replicatedFiles[i].getAbsolutePath());

                    String fileName = replicatedFiles[i].getName();
                    String nodeIp = INameServer.getFileIp(fileName);
                    int nodeHash = INameServer.getHashOfIp(nodeIp);
                    INode nodeRMI = (INode) Naming.lookup("//"+nodeIp+"/"+nodeHash);
                    nodeRMI.deleteFileLocation(fileName, Main.INode.getId());

                } else {
                    tcpSender.SendFile(ipPrevNode, replicatedFiles[i].getAbsolutePath());
                    if (fileMap.containsKey(replicatedFiles[i].getName())) {
                        passFiche(replicatedFiles[i].getName(), ipPrevNode);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File[] localFiles = localFolder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            try {
                String fileName = localFiles[i].getName();
                String nodeIp = INameServer.getFileIp(fileName);
                int nodeHash = INameServer.getHashOfIp(nodeIp);
                INode nodeRMI = (INode) Naming.lookup("//"+nodeIp+"/"+nodeHash);
                nodeRMI.deleteFileLocation(fileName, Main.INode.getId());
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void setNodeName(String name){
        this.nodeName = name;
    }

    public void createFicheOnStartup(){
        try{
            //System.out.println("MAIN: creating fiches for local files");
            fileMap = new TreeMap<>();
            File[] listOfFiles = localFolder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    FileMap f = new FileMap(file.getName(),INameServer.getNodeIp(Main.INode.getId()),Main.INode.getId());
                    fileMap.put(file.getName(),f); // voeg toe aan eigen fichemap
                }
            }

            System.out.println("REPLICATION fiches on startup <" +fileMap+ ">");
        }
        catch(Exception e){}
    }
    public void passFiche(String file, String ownerIp){
        try {
            int hash = INameServer.getHashOfIp(ownerIp);
            fileMap.get(file).addLocation(ownerIp,hash);                  // add new owner to locations
            INode INodeNew = (INode) Naming.lookup("//"+ownerIp+"/"+Integer.toString(hash));
            INodeNew.sendFiche(fileMap.get(file));                          // send fiche to new owner
            fileMap.remove(file); // remove fiche from own fichemap
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteFile(String filename){
        HashMap<Integer, String> locations =  fileMap.get(filename).getFileLocations();
        Iterator<Integer> keySetIterator = locations.keySet().iterator();
        while (keySetIterator.hasNext()) {
            int hash = keySetIterator.next();
            try {
                if(hash != Main.INode.getId()){
                    String ip = locations.get(hash);
                    INode INodeOwner = (INode) Naming.lookup("//"+ip+"/"+hash);
                    INodeOwner.deleteLocalFile(filename);
                } else {
                    UserInterface.getFile(filename).delete();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
/*
            Als ge iets uit de bestandsfiche nodig hebt
            fileMap.get("filenaam").method_da_ge_nodig_hebt();
            vb zie in createFicheOnStartup() => onder printen vn fiches
 */