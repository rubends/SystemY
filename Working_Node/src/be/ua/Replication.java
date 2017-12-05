package be.ua;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.TreeMap;

public class Replication {
    NameServerInterface INameServer;
    String nodeName;
    String rootPath = new File("").getAbsolutePath();
    String sep = System.getProperty("file.separator");
    File localFolder = new File(rootPath + sep + "Files" + sep + "Local");
    File replicationFolder = new File(rootPath + sep + "Files" + sep + "Replication");
    Node Node;
    private INode INodeNew;
    public Replication(NameServerInterface ns) {
        INameServer = ns;
    }
    public void  createFicheOnStartup(){
        System.out.println("MAIN: creating fiches for local files");
        try{
            Node.ficheMap = new TreeMap<>();
        }
        catch(NullPointerException np){System.out.println("NP on creating treemap");}
        File[] localFiles = localFolder.listFiles();
        for (File file : localFiles) {
            if (file.isFile()) {
                //file fiche aanmaken en toevoegen aan lijst
                try{
                    FileMap f = new FileMap(file.getName(),"",Node.getId());
                    Node.ficheMap.put(file.getName(),f); // voeg toe aan eigen fichemap
                }
                catch(NullPointerException NP2){
                    System.out.println("NP with getting node");
                }

            }
        }
        System.out.println("MAIN: fiches on statup <" +Node.ficheMap+ ">");
    }
    public void getFiles() {
        File[] localFiles = localFolder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            /*
            //replicate(localFiles[i].getName(), localFiles[i].getAbsolutePath());
            //Reden voor uitcommenten is omda ik anders ni weet hoedk in die functie correct element kan aanspreken uit lijst
            */
            try {
                String ip = INameServer.getFileIp(localFiles[i].getName());
                int ownHash = INameServer.getHashOfName(nodeName);
                String ownIp = INameServer.getNodeIp(ownHash);
                TCPSender tcpSender = new TCPSender(7896);
                if(!ip.equals(ownIp)){
                    tcpSender.SendFile(ip, localFiles[i].getAbsolutePath());

                    INodeNew = (INode) Naming.lookup("//"+ownIp+"/"+ownHash);
                    //maak fiche voor nieuwe owner
                    FileMap fiche = Node.ficheMap.get(localFiles[i].getName());
                    fiche.addLocation(ownIp, ownHash);
                    //nu fiche lokaal verwijderen ->is geen owner meer
                    System.out.println("REPLICATIONMANAGER: deleting '"+fiche.getFilename()+"' from fichemap and sending to '"+ownIp+"'");
                    INodeNew.sendFiche(fiche);
                    Node.ficheMap.remove(localFiles[i].getName());

                } else {
                    ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(ownHash);
                    int neighbourHash = neighbours.get(0);
                    System.out.println("neighbourHash = "+ neighbourHash);
                    String prevIp = INameServer.getNodeIp(neighbourHash);

                    FileMap fiche = Node.ficheMap.get(localFiles[i].getName());
                    fiche.addLocation(prevIp, neighbourHash);
                    Node.ficheMap.put(localFiles[i].getName(), fiche);

                    tcpSender.SendFile(prevIp, localFiles[i].getAbsolutePath());
                }
                //@todo BESTANDSFICHE UPDATEN --> Sam
            } catch (Exception e){
                e.printStackTrace();
            }

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
            TCPSender tcpSender = new TCPSender(7896);
            for (int i = 0; i < replicatedFiles.length; i++) {
                String ipOwner = INameServer.getFileIp(replicatedFiles[i].getName());
                if(ipNextNode.equals(ipOwner)){
                    tcpSender.SendFile(ipNextNode, replicatedFiles[i].getAbsolutePath());
                    //replicatedFiles[i].delete(); nodig?
                    //@todo BESTANDSFICHE UPDATEN --> Sam
                    if(Node.ficheMap.containsKey(replicatedFiles[i].getName()))
                    {
                        passFiche(replicatedFiles[i].getName(),ipNextNode);
                    }
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
    public void passFiche(String file, String newNodeName){
        System.out.println("PASSFICHE OPEGROEPEN");
        try{
            int hash = INameServer.getHashOfName(newNodeName);
            String connName = Integer.toString(hash);
            String NodeIp = INameServer.getNodeIp(hash);
            INodeNew = (INode) Naming.lookup("//"+NodeIp+"/"+connName);

            Node.ficheMap.get(file).addLocation(NodeIp,hash);                                   // add new owner to DLlocations
            INodeNew.sendFiche(Node.ficheMap.get(file));                                        // send fiche to new owner
            Node.ficheMap.remove(file);                                                         // remove fiche from own fichemap
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void setNodeName(String name){
        this.nodeName = name;
    }
}