package be.ua;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.TreeMap;

public class NameServer implements NameServerInterface {

    private TreeMap<Integer, String> nodeMap;

    protected NameServer() throws RemoteException {
        super();
        nodeMap = new TreeMap<Integer, String>(); //create map
    }

    public String getFileIp(String fileName) throws RemoteException {
        int hash = getHashOfName(fileName);
        int mapKey = 0;

        Iterator keys = nodeMap.entrySet().iterator();
        if(hash < nodeMap.firstKey()){
            mapKey = nodeMap.lastKey();
        }

        //while (keys.hasNext()) {
//            int key = keys.next();
//            if(key < hash || key == hash)
//            {
//                mapKey = key;
//            }
        //}
        System.out.println("IP of node: " + nodeMap.get(mapKey) + "\n");
        return nodeMap.get(mapKey);
    }

    public void addNode(String nodeName, String nodeIP) {
        try {
            int hash = getHashOfName(nodeName);
            if (nodeMap.get(hash) == null) { //check if already exists
                nodeMap.put(hash, nodeIP);
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./nodeMap.ser"));
                out.writeObject(nodeMap);
                out.close();
                System.out.println("Succesfully added " + nodeName);
            } else {
                System.out.println(nodeName + " already exists");
            }
        } catch (Exception e) { }
    }

    public void deleteNode(String nodeName){
        try {
            int hash = getHashOfName(nodeName);
            if (nodeMap.get(hash) != null) { //the to be deleted file exists
                nodeMap.remove(hash);
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./nodeMap.ser"));
                out.writeObject(nodeMap);
                out.close();
                System.out.println("Succesfully deleted " + nodeName);
            }else  {
                System.out.println(nodeName + " doesn't exist and therefore can't be deleted.");
            }
        }
        catch( Exception e){}
    };

    public void printNodeMap(){
        System.out.println("\nList of nodes in the node map:");
        Iterator<Integer> keySetIterator = nodeMap.keySet().iterator();
        while (keySetIterator.hasNext()) {
            int key = keySetIterator.next();
            System.out.println("Hash: " + key + "\tIP: " + nodeMap.get(key));
        }
    }

    public static int getHashOfName(String name) {
        return Math.abs(name.hashCode() % 32769);
    }

    public static void main(String[] args) {
        String registryName = "nodeNames";
        try
        {
            NameServerInterface ns = new NameServer();
            NameServerInterface stub = (NameServerInterface) UnicastRemoteObject.exportObject(ns, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(registryName, stub);
            System.out.println("Nameserver bound");


            //test add, delete and print nodemap
            ns.addNode("name","192.168.1.1");
            ns.addNode("name","192.168.1.1");
            ns.addNode("naame","192.168.1.1");
            ns.printNodeMap();
            ns.deleteNode("naame");
            ns.printNodeMap();

        }
        catch (Exception e)
        {
            System.out.println("Nameserver error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //src: http://www.tutorialspoint.com/java/java_serialization.htm
}
