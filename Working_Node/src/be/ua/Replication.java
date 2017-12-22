package be.ua;

import java.io.File;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.TreeMap;

public class Replication {
    private NameServerInterface INameServer;
    private String nodeName;
    private String rootPath = new File("").getAbsolutePath();
    private String sep = System.getProperty("file.separator");
    private File localFolder = new File(rootPath + sep + "Files" + sep + "Local");
    private File replicationFolder = new File(rootPath + sep + "Files" + sep + "Replication");
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
                //TODO: CHECK IF FICHE MAP IS CORRECT

                tcpSender.SendFile(ip, location);
            } else {
                //ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(ownHash);
                int neighbourHash = Main.INode.getPrevNode();
                if(ownHash != neighbourHash){
                    String prevIp = INameServer.getNodeIp(neighbourHash);
                    System.out.println("REPLICATION file " + filename + " replicated to previous node " + prevIp);
                    FileMap fiche = fileMap.get(filename);
                    fiche.addLocation(ip, hash);
                    fileMap.put(filename, fiche);

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
            System.out.println("REPLICATION ip sam: " + ipNextNode);
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
                            //TODO: CHECK IF FICHE MAP IS CORRECT
                            //if (replicatedFiles[i].isFile()) replicatedFiles[i].delete(); //NO DELETE, NEW DOWNLOADLOCATION
                            //FICHE DOORSTUREN + TOEVOEGEN AAN LIJST
                        }
                    }
                } else if (prevIp.equals(ipOwner)) { //files die op deze node staan, omdat hun hash overeen komt met de node waar ze lokaal op stonden, moeten ook gecheckt worden

                }
            }
            if(MulticastThread.nodeCount == 2) { // WEL LOCAL FILES CHECKEN WANNEER ER 1 BUUR IS.
                for (int i = 0; i < localFiles.length; i++) {
                    String ipOwner = INameServer.getFileIp(localFiles[i].getName());
                    if (ipNextNode.equals(ipOwner)) {
                        System.out.println("REPLICATION sending local file: " + localFiles[i].getName() + " to " + ipNextNode + " if it equals " + ipOwner);
                        tcpSender.SendFile(ipNextNode, localFiles[i].getAbsolutePath());
                        if (fileMap.containsKey(replicatedFiles[i].getName())) {
                            passFiche(localFiles[i].getName(), ipNextNode); //FICHE DOORSTUREN + TOEVOEGEN AAN LIJST
                        }
                    } else if (ownIp.equals(ipOwner)) { // !!!! WHEN LOCAL FILES ARE BELONGING TO OWN NODE, THEY ARE REPLICATED TO NEW NODE
                        System.out.println("REPLICATION sending local file: " + localFiles[i].getName() + " to " + prevIp);
                        tcpSender.SendFile(prevIp, localFiles[i].getAbsolutePath());
                        passFiche(localFiles[i].getName(), prevIp);
                        //if Ip is from own node, send to prev node
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
                    if (replicatedFiles[i].isFile()) replicatedFiles[i].delete();
                } else {
                    tcpSender.SendFile(ipPrevNode, replicatedFiles[i].getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File[] localFiles = localFolder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            System.out.println("REPLICATION name: "+ localFiles[i].getName());
            System.out.println("REPLICATION in map: "+ fileMap.get(localFiles[i].getName()));
            String nodeIp = fileMap.get(localFiles[i].getName()).getIpOfLocation();
            int nodeHash = fileMap.get(localFiles[i].getName()).getHashOfLocation();

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
}
/*
            Als ge iets uit de bestandsfiche nodig hebt
            fileMap.get("filenaam").method_da_ge_nodig_hebt();
            vb zie in createFicheOnStartup() => onder printen vn fiches
 */