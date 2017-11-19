package be.ua;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public class Replication {
    NameServerInterface INameServer;
    File[] localFiles;
    File[] replicatedFiles;

    public Replication(NameServerInterface ns) {
        INameServer = ns;
        getFiles();
    }

    public void getFiles() {
        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator");
        File folder = new File(rootPath + sep + "Files" + sep + "Local");
        localFiles = folder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            //!!!!!!!!!!!!!!!!!!!!! fotos werken nog niet
            replicate(localFiles[i].getName(), localFiles[i].getAbsolutePath());
        }
    }

    public void replicate(String filename, String location){
        System.out.println(filename);
        try {
            String ip = INameServer.getFileIp(filename);
            TCPSender tcpSender = new TCPSender(7896);
            //!!!!!!!!!!!!!!!!!!!!!! if location is zichzelf --> naar de vorige node
            tcpSender.SendFile(ip, location);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void prevNode(int hashNextNode){
        /*
        In deze functie is het de bedoeling dat:
        • De node vraagt het IP-adres van de eigenaar van ieder gerepliceerd bestand op bij de name server. = OK
        • Dan vraagt het hij IP op aan de hand van de hash van de volgende node. = OK
        • Als deze twee gelijk zijn aan elkaar, repliceert hij het bestand naar zijn nieuwe volgende node = OK?
        • Als de huidige node eigenaar was van het bestand, zal hij zijn fiche mee doorgeven naar de nieuwe eigenaar. = NOK
         */
        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator");
        File folder = new File(rootPath + sep + "Files" + sep + "Replication");
        replicatedFiles = folder.listFiles();
        for (int i = 0; i < replicatedFiles.length; i++) {
            try{
                String ipOwner = INameServer.getFileIp(replicatedFiles[i].getName());
                String ipNextNode = INameServer.getNode(hashNextNode);
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
