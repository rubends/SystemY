package be.ua;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
        //Comparator<File> fileListComp = Comparator.comparing(File::getName); // compares the file in de treemap by name
        //fileList = new TreeMap<>(fileListComp);
        fileList = new TreeMap<>();
    }

    public void updateNeighbours(int newPrevious, int newNext)
    {
        this.mNext = newNext;
        this.mPrevious = newPrevious;
    }

    public void updateNextNode(int newNext) {
        this.mNext = newNext;
    }

    public int getNextNode() {
        return this.mNext;
    }

    public void updatePrevNode(int newPrev) {
        this.mPrevious = newPrev;
    }

    public int getPrevNode() {
        return this.mPrevious;
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

    public boolean hasFile(String fileName){
        boolean hasFile = false;
        if(Replication.fileMap.containsKey(fileName)){
            hasFile = true;
        }
        return hasFile;
    }

    public void shutdown() throws RemoteException{
        System.out.println("Shutting down node...");
        try {
            int prevHash = mPrevious;
            int nextHash = mNext;

            //inform previous node + replicate files to previous node
            if(prevHash != Node.nodeHash){
                String prevIp = INameServer.getNodeIp(prevHash);
                INode prevNode = (INode) Naming.lookup("//"+prevIp+"/"+Integer.toString(prevHash));
                prevNode.updateNextNode(nextHash);

                Replication replication = new Replication(INameServer);
                replication.toPrevNode(prevHash);
            }

            //inform next node
            if(nextHash != Node.nodeHash)
            {
                String nextIp = INameServer.getNodeIp(nextHash);
                INode nextNode = (INode) Naming.lookup("//"+nextIp+"/"+Integer.toString(nextHash));
                nextNode.updatePrevNode(prevHash);
            }

            INameServer.deleteNode(Node.nodeHash);
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void failure(int hashOfFailedNode) throws RemoteException{
        System.out.println("Node " + mId + " is taking care of node " + hashOfFailedNode + " that failed.");
        try {
            ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(hashOfFailedNode);
            int prevHashOfFailedNode = neighbours.get(0);
            int nextHashOfFailedNode = neighbours.get(1);

            //node lifecycle
            if(prevHashOfFailedNode != Node.nodeHash){ //check that previous node of the failed node is not you
                String prevIp = INameServer.getNodeIp(prevHashOfFailedNode);
                INode prevNode = (INode) Naming.lookup("//"+prevIp+"/"+Integer.toString(prevHashOfFailedNode));
                prevNode.updateNextNode(nextHashOfFailedNode);
            } else {
                updateNextNode(nextHashOfFailedNode);
            }
            if(nextHashOfFailedNode != Node.nodeHash) //check that next node of the failed node is not you
            {
                String nextIp = INameServer.getNodeIp(nextHashOfFailedNode);
                INode nextNode = (INode) Naming.lookup("//"+nextIp+"/"+Integer.toString(nextHashOfFailedNode));
                nextNode.updatePrevNode(prevHashOfFailedNode);
            } else {
                updatePrevNode(prevHashOfFailedNode);
            }
            INameServer.deleteNode(hashOfFailedNode);

            //start agent
            FailureAgent failureAgent = new FailureAgent(hashOfFailedNode, INameServer);
            RMIAgent rmiAgent = new RMIAgent(Main.INode, INameServer);
            rmiAgent.passFailureAgent(failureAgent);

        } catch (Exception e) { e.printStackTrace(); }
    }
}
