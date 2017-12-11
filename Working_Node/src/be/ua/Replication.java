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

            int hash = INameServer.getHashOfIp(ip);//TODO: ER MOET NOG EEN FUNCTIE GEMAAKT WORDEN OM HASHES OP TE HALEN!

            if(!ip.equals(ownIp)){
                //maak fiche voor nieuwe owner
                FileMap fiche = fileMap.get(filename);
                fiche.addLocation(ip, hash);

                //nu fiche lokaal verwijderen ->is geen owner meer
                System.out.println("REPLICATION: deleting '"+ fiche.getFilename() +"' from fichemap and sending to '"+ip+"'");
                INode INodeNew = Main.nodeMap.get(hash);
                INodeNew.sendFiche(fiche);
                fileMap.remove(filename);

                tcpSender.SendFile(ip, location);
            } else {
                ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(ownHash);
                int neighbourHash = neighbours.get(0);
                String prevIp = INameServer.getNodeIp(neighbourHash);

                System.out.println("REPLICATION: this node is owner sending to previous node: "+ filename + " " + ip);
                FileMap fiche = fileMap.get(filename);
                fiche.addLocation(ip, hash);
                fileMap.put(filename, fiche);

                tcpSender.SendFile(prevIp, location);
            }
            //@todo BESTANDSFICHE UPDATEN --> ZIE HIERBOVEN
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void toNextNode(int hashNextNode){ //Nieuwe node komt erbij
        File[] replicatedFiles = replicationFolder.listFiles();
        try {
            String ipNextNode = INameServer.getNodeIp(hashNextNode);
            TCPSender tcpSender = new TCPSender(SOCKET_PORT);
            for (int i = 0; i < replicatedFiles.length; i++) {
                String ipOwner = INameServer.getFileIp(replicatedFiles[i].getName());
                if(ipNextNode.equals(ipOwner)){
                    tcpSender.SendFile(ipNextNode, replicatedFiles[i].getAbsolutePath());
                    //replicatedFiles[i].delete(); nodig?
                    //@todo BESTANDSFICHE UPDATEN --> ZIE HIERONDER
                    if(fileMap.containsKey(replicatedFiles[i].getName()))
                    {
                        //FICHE DOORSTUREN + TOEVOEGEN AAN LIJST
                        passFiche(replicatedFiles[i].getName(),ipNextNode);
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
                } else {
                    tcpSender.SendFile(ipPrevNode, replicatedFiles[i].getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File[] localFiles = localFolder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            String nodeIp = fileMap.get(localFiles[i]).getIpOfLocation();//@todo GET ALLE USER IP'S VAN FILE UIT BESTANDSFICHE --> ZIE Hiernaast
            int nodeHash = fileMap.get(localFiles[i]).getHashOfLocation(); //@todo GET ALLE USER HASHED VAN FILE UIT BESTANDSFICHE --> ZIE hiernaast


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
            System.out.println("MAIN: creating fiches for local files");
            fileMap = new TreeMap<>();
            File[] listOfFiles = localFolder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    //file fiche aanmaken en toevoegen aan lijst
                    FileMap f = new FileMap(file.getName(),"test",Main.INode.getId()); //TODO:NIET MET TEST_ EIGEN IP!
                    fileMap.put(file.getName(),f); // voeg toe aan eigen fichemap
                }
            }
            System.out.println("MAIN: fiches on startup <" +fileMap+ ">");
            fileMap.get("test7.txt").getIpOfLocation();
            fileMap.get("test7.txt").getHashOfLocation();
            fileMap.get("test7.txt").printLocation();
        }
        catch(Exception e){}
    }
    public void passFiche(String file, String ownerIp){
        System.out.println("PASSFICHE OPEGROEPEN");
        try {
            int hash = 0; //HIER MOET DE HASH OPGEHAALD WORDEN DIE BIJ IP HOORT?
            fileMap.get(file).addLocation(ownerIp,hash);                  // add new owner to locations
            INode INodeNew = Main.nodeMap.get(hash);
            INodeNew.sendFiche(fileMap.get(file));            // send fiche to new owner
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