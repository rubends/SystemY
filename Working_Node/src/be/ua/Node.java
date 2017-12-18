package be.ua;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Comparator;
import java.util.TreeMap;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int mPrevious;
    public volatile int mNext;
    private volatile int mId;
    private volatile NameServerInterface INameServer;
    public static TreeMap<File, Boolean> fileList; // file - locked
    public static int nodeHash; //for local hash getting


    protected Node(int hash, NameServerInterface ns) throws RemoteException
    {
        super();
        mId = mPrevious = mNext = nodeHash = hash;
        INameServer = ns;
        Comparator<File> fileListComp = Comparator.comparing(File::getName); // compares the file in de treemap by name
        fileList = new TreeMap<>(fileListComp);
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

    public void nodeShutdownFiles(int hash) {
        //@todo replication shutdown: update in filemap dat de eigenaar van 'hash' er niet meer is
        //updateFiche( fileName,  hash,  ipLocation)
    }

    public void sendFiche(FileMap fiche) {
        Replication.fileMap.put(fiche.getFilename(),fiche);
        System.out.println("nieuwe item toegevoegd aan map" + fiche.getFilename());
    }

    public void updateFiche(String fileName, int id, String ipLocation){
        if(Replication.fileMap.containsKey(fileName)){
            //fiche bestaat
            Replication.fileMap.get(fileName).addLocation(ipLocation,id);
        }else{
            //fiche bestaat niet
            //fiche toevoegen
            FileMap tmpFiche = new FileMap(fileName,"",0);
            tmpFiche.addLocation(ipLocation,id);
            Replication.fileMap.put(fileName,tmpFiche);
        }
    }

    public boolean hasFile(File file){
        boolean hasFile = false;
        if(fileList.containsKey(file)){
            hasFile = true;
        }
        return hasFile;
    }

}
