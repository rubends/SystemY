package be.ua;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.TreeMap;

public class Node extends UnicastRemoteObject implements INode{
    private volatile int mPrevious;
    private volatile int mNext;
    private volatile int mId;
    private volatile NameServerInterface INameServer;
    private volatile TreeMap<String, Boolean> localFileList; // filename - locked
    public static int nodeHash; //for local hash getting

    protected Node(int hash, NameServerInterface ns) throws RemoteException
    {
        super();
        mId = mPrevious = mNext = nodeHash = hash;
        INameServer = ns;
        localFileList = new TreeMap<>();
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

    public void deleteFileLocation(String filename, int nodeHash) {
        Replication.fileMap.get(filename).removeLocation(nodeHash); // TODO NOT FOUND NULLPOINTER
    }

    public void sendFiche(FileMap fiche) {
        System.out.println("Getting fiche from " + fiche.getFilename());
        Replication.fileMap.put(fiche.getFilename(),fiche);
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
        } else {
            File localFolder = new File(Main.pathToLocalFiles);
            hasFile = new File(localFolder, fileName).exists();
        }
        return hasFile;
    }

    public void shutdown() throws RemoteException{
        System.out.println("Shutting down node...");
        try {
            int prevHash = mPrevious;
            int nextHash = mNext;

            //inform previous node + replicate files to previous node
            if(prevHash != mId){
                String prevIp = INameServer.getNodeIp(prevHash);
                INode prevNode = (INode) Naming.lookup("//"+prevIp+"/"+Integer.toString(prevHash));
                prevNode.updateNextNode(nextHash);

                Replication replication = new Replication(INameServer);
                replication.toPrevNode(prevHash);
            }

            //inform next node
            if(nextHash != mId)
            {
                String nextIp = INameServer.getNodeIp(nextHash);
                INode nextNode = (INode) Naming.lookup("//"+nextIp+"/"+Integer.toString(nextHash));
                nextNode.updatePrevNode(prevHash);
            }

            INameServer.deleteNode(mId);
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
            if(prevHashOfFailedNode != mId){ //check that previous node of the failed node is not you
                String prevIp = INameServer.getNodeIp(prevHashOfFailedNode);
                INode prevNode = (INode) Naming.lookup("//"+prevIp+"/"+Integer.toString(prevHashOfFailedNode));
                prevNode.updateNextNode(nextHashOfFailedNode);
            } else {
                updateNextNode(nextHashOfFailedNode);
            }

            if(nextHashOfFailedNode != mId) //check that next node of the failed node is not you
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
            RMIAgent rmiAgent = new RMIAgent(INameServer);
            rmiAgent.startFailureAgent(failureAgent);

        } catch (Exception e) { e.printStackTrace(); }
    }

    public String getDownloadLocation(String filename){
        return Replication.fileMap.get(filename).getIpOfLocation();
    }

    public void sendFile(String ip, String filename){
        TCPSender tcpSender = new TCPSender(7897);
        tcpSender.downloadRequest(filename, ip);
    }

    public void deleteFile(String filename){
        Replication.deleteFile(filename);
        Replication.fileMap.remove(filename);
    }

    public void deleteLocalFile(String filename){
        File file = UserInterface.getFile(filename);
        file.delete();
    }


    public void setLocalFileList( TreeMap<String, Boolean> fileList){
        this.localFileList = fileList;
    }
    public TreeMap<String, Boolean> getLocalFileList(){
        return this.localFileList;
    }
}
