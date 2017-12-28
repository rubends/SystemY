package be.ua;

import java.io.File;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.TreeMap;

public class FailureAgent implements Runnable, Serializable {

    //port declaration
    private final int SOCKET_PORT = 7896;

    //attributes
    public int failureNode; //hash of the failed node - from constructor
    public int agentNode; //hash of the node that started the failureagent

    NameServerInterface INameServer; //from constructor
    TreeMap<String, Boolean> fileList; //global filelist, FORMAT: filename - isLocked

    public FailureAgent(int failureN, NameServerInterface nameServerInterface) {
        failureNode = failureN;
        INameServer = nameServerInterface;
    }

    /* == Wat run() moet kunnen ==
    i. Bekijk heel de lijst van bestanden van de huidige node.
    ii. Als de hash van een bestandsnaam op de falende node terecht komt zijn er 2 mogelijkheden:

        1.  Stuur bestand door naar de nieuwe eigenaar, als de nieuwe eigenaar nog geen
            eigenaar is van het bestand en niet over het bestand beschikt. Update bij de
            nieuwe eigenaar (bestandsfiche) de informatie over waar het bestand
            beschikbaar is in het systeem met als downloadlocatie de huidige node

        2.  Als de nieuwe eigenaar al eigenaar is door vorige acties van agent, update de
            nieuwe eigenaar met de informatie dat het bestand beschikbaar is op de huidige node

     iii. Als de huidige node gelijk is aan de node waarop de agent gestart is, wordt de agent afgesloten
     */

    @Override
    public void run() {
        try{
            fileList = Main.INode.getLocalFileList();
            agentNode = Node.nodeHash;
        }catch(RemoteException e ){}


        
        Iterator<String> keySetIterator = fileList.keySet().iterator();
        while (keySetIterator.hasNext()) {
            String fileName = keySetIterator.next();
            try {
                String fileIP = INameServer.getFileIp(fileName);
                int fileHash = INameServer.getHashOfName(fileName);
                String failureIP = INameServer.getNodeIp(failureNode);
                if(fileIP.equals(failureIP)){
                    System.out.println(fileName + " is owned by " + failureNode + " at " + failureIP);
                    int newOwner = INameServer.getNeighbourNodes(failureNode).get(0);
                    String newOwnerIP = INameServer.getNodeIp(newOwner);
                    INode newOwnerNode = (INode) Naming.lookup("//" + newOwnerIP + "/" + newOwner);
                    if(!newOwnerNode.hasFile(fileName)){
                        TCPSender tcpSender = new TCPSender(SOCKET_PORT);
                        tcpSender.SendFile(newOwnerIP, UserInterface.getFile(fileName).getAbsolutePath());
                    }
                    newOwnerNode.updateFiche(fileName, fileHash, newOwnerIP);
                }
            } catch (Exception e) {e.printStackTrace();}
        }
    }
}
