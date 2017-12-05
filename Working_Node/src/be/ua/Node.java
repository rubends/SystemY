package be.ua;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int mPrevious;
    private volatile int mNext;
    private volatile int mId;
    private volatile NameServerInterface INameServer;
    private volatile TreeMap<Integer, INode> nodeMap;
    public static volatile TreeMap<String, FileMap> ficheMap;


    protected Node(int hash, TreeMap otherNodes, NameServerInterface ns) throws RemoteException
    {
        super();
        mId = mPrevious = mNext = hash;
        nodeMap = otherNodes;
        INameServer = ns;
    }

    public void updateNeighbours(int newPrevious, int newNext)
    {
        this.mNext = newNext;
        this.mPrevious = newPrevious;
    }

    public void updateNextNode(int newNext) {
        this.mNext = newNext;
    }

    public void updatePrevNode(int newPrev) {
        this.mPrevious = newPrev;
    }

    public int getId(){
        return mId;
    }

    public void shutdown() throws RemoteException{
        INode previousNode = nodeMap.get(mPrevious);
        previousNode.updateNextNode(mNext);

        INode nextNode = nodeMap.get(mNext);
        nextNode.updatePrevNode(mPrevious);

        Replication replication = new Replication(INameServer);
        replication.toPrevNode(this.mPrevious);

        INameServer.deleteNode(mId);
        System.exit(0);
    }

    public void addNodeToMap(int hash, INode node){
        this.nodeMap.put(hash, node);
    }

    public void sendFileMap(String fileName){
        //int hashLocation = Filemap.getLocationLocal(fileName);
        //Filemap.passFiche(fileName,hashLocation,true);
        //hashLocation = Filemap.getLocationRepli(fileName);
        //Filemap.passFiche(fileName,hashLocation,false);
    }

    private void setupRMI(String nodeName, int nodeCount) throws NotBoundException {
        RMIConnector connectorNode = new RMIConnector(INameServer, nodeName, nodeCount);
    }
    public void sendFiche(FileMap fiche){
        ficheMap.put(fiche.getFilename(),fiche);
        System.out.println("new item added to map" + fiche.getFilename());
    }

    public void updateFiche(String fileName, int id, String ipLocation){
        if(ficheMap.containsKey(fileName)){
            //fiche bestaat
            ficheMap.get(fileName).addLocation(ipLocation,id);
        }else{
            //fiche bestaat niet
            //fiche toevoegen
            FileMap tmpFiche = new FileMap(fileName,"",0);
            tmpFiche.addLocation(ipLocation,id);
            ficheMap.put(fileName,tmpFiche);
        }
    }
}
