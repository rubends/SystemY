package be.ua;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class NameServer extends UnicastRemoteObject implements NameServerInterface {

    private TreeMap<Integer, String> nodeMap;


    protected NameServer() throws RemoteException
    {
        super();
        nodeMap = new TreeMap<>();                              //create map
    }

    public String getFileIp(String fileName) throws RemoteException
    {
        int hash = getHashOfName(fileName);
        int mapKey = 0;

        Iterator<Map.Entry<Integer, String>> keys = nodeMap.entrySet().iterator();
        if(hash < nodeMap.firstKey())                           //if the hash is smaller than the smallest node hash
        {
            mapKey = nodeMap.lastKey();                         //then it goes to the last node
        }

        while (keys.hasNext())
        {
            Map.Entry<Integer, String> key = keys.next();
            int keyHash = key.getKey();
            if(keyHash < hash || keyHash == hash)
            {
                mapKey = keyHash;
            }
        }
        System.out.println("IP of node: " + nodeMap.get(mapKey) + "\n");
        return nodeMap.get(mapKey);
    }

    public void addNode(String nodeName, String nodeIP)
    {
        try {
            int hash = getHashOfName(nodeName);
            if (nodeMap.get(hash) == null) {                    //check if already exists
                nodeMap.put(hash, nodeIP);
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./nodeMap.ser"));
                out.writeObject(nodeMap);
                out.close();
                System.out.println("Succesfully added: " + nodeName);
            } else {
                System.out.println("This node already exists: "+ nodeName);
            }
        } catch (Exception e) {
            System.out.println("Nameserver - addNode error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteNode(String nodeName)
    {
        try {
            int hash = getHashOfName(nodeName);
            if (nodeMap.get(hash) != null) {                    //the to be deleted file exists
                nodeMap.remove(hash);
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./nodeMap.ser"));
                out.writeObject(nodeMap);
                out.close();
                System.out.println("Succesfully deleted: " + nodeName);
            }else  {
                System.out.println("This node doesn't exist and therefore can't be deleted: " + nodeName);
            }
        }
        catch( Exception e){
            System.out.println("Nameserver - deleteNode error: " + e.getMessage());
            e.printStackTrace();
        }
    };

    public void printNodeMap()
    {
        System.out.println("List of nodes in the node map:");
        Iterator<Integer> keySetIterator = nodeMap.keySet().iterator();
        while (keySetIterator.hasNext()) {
            int key = keySetIterator.next();
            System.out.println("Hash: " + key + "\tIP: " + nodeMap.get(key));
        }
    }

    public int getNodeCount()
    {
        int nodeCount = nodeMap.size();
        System.out.println("Number of nodes: " + nodeCount);
        return nodeCount;
    }

    public int getHashOfName(String name) {
        return Math.abs(name.hashCode() % 32769);
    }

    public ArrayList getNeighbourNodes(int hash){
        ArrayList<Integer> neighbours = new ArrayList<>();

        neighbours.add(nodeMap.lowerKey(hash));
        neighbours.add(nodeMap.higherKey(hash));

        return neighbours;
    }
}
