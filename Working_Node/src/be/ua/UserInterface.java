package be.ua;

import java.io.File;
import java.rmi.Naming;
import java.util.*;
import java.lang.String;

public class UserInterface {
    private NameServerInterface INameServer;

    public UserInterface(NameServerInterface NameServerInterface) {
        INameServer = NameServerInterface;
        System.out.println("Startup of node");
    }

    public void startUI() {
        while(true) {
            Scanner input = new Scanner(System.in);
            System.out.println("__________________________________________ ");
            System.out.print("Choose Action:\n" +
                    "\t0. Shutdown\n" +
                    "\t1. Print neighbours\n" +
                    "\t2. Print system file list\n" +
                    "\t3. Print file fiches\n" +
                    "\t4. Open file \n" +
                    "\t5. Delete file \n" +
                    "\t6. Node failure\n");
            System.out.println(" > ");

            //@todo MORE TESTS!
            int action = input.nextInt();
            try {
                if (action == 0) {
                    shutdown();
                } else if (action == 1) {
                    ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(Node.nodeHash);
                    System.out.println("previous node: " + neighbours.get(0));
                    System.out.println("next node: " + neighbours.get(1));
                } else if (action == 2) {
                    if(Node.fileList.size() > 0) {
                        for (Map.Entry<File, Boolean> entry : Node.fileList.entrySet()) {
                            System.out.println("Name: " + entry.getKey().getName() + ". Locked: " + entry.getValue());
                        }
                    } else {
                        System.out.println("No files found in the system file list.");
                    }
                } else if (action == 3) {
                    if(Replication.fileMap.size() > 0) {
                        for (Map.Entry<String, FileMap> entry : Replication.fileMap.entrySet()) {
                            System.out.println("File: " + entry.getKey() + " on location: " + entry.getValue().getIpOfLocation() + " with hash " + entry.getValue().getHashOfLocation());
                        }
                    } else {
                        System.out.println("No files found in the file fiches.");
                    }
                } else if (action == 4) {
                    System.out.println("Give filename:");
                    String file = input.next();
                    String ip = INameServer.getFileIp(file);
                    System.out.println("File is located at " + ip);
                    //todo get file
                } else if (action == 5) {
                    System.out.println("Give filename:");
                    String file = input.next();
                    String ip = INameServer.getFileIp(file);
                    //todo delete file
                } else if (action == 6) {
                    System.out.println("Give failed node hash:");
                    int nodeHash = input.nextInt();
                    failure(nodeHash);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void shutdown(){
        System.out.println("Shutting down node");
        try {
            ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(Node.nodeHash);
            int prevHash = neighbours.get(0);
            int nextHash = neighbours.get(1);
            if(prevHash != Node.nodeHash){
                String prevIp = INameServer.getNodeIp(prevHash);
                INode prevNode = (INode) Naming.lookup("//"+prevIp+"/"+Integer.toString(prevHash));
                prevNode.updateNextNode(nextHash);

                Replication replication = new Replication(INameServer);
                replication.toPrevNode(prevHash);
            }
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

    public void failure(int hash){
        System.out.println("Node " + hash + " failed.");
        try {
            ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(hash);
            int prevHash = neighbours.get(0);
            int nextHash = neighbours.get(1);
            if(prevHash != Node.nodeHash){
                String prevIp = INameServer.getNodeIp(prevHash);
                INode prevNode = (INode) Naming.lookup("//"+prevIp+"/"+Integer.toString(prevHash));
                prevNode.updateNextNode(nextHash);
            } else {
                //update own next node
            }
            if(nextHash != Node.nodeHash)
            {
                String nextIp = INameServer.getNodeIp(nextHash);
                INode nextNode = (INode) Naming.lookup("//"+nextIp+"/"+Integer.toString(nextHash));
                nextNode.updatePrevNode(prevHash);
            } else {
                //update own prev node
            }

            INameServer.deleteNode(hash);

            FailureAgent failureAgent = new FailureAgent(hash, INameServer);
            Thread agentThread = new Thread(failureAgent);
            agentThread.start();
            //@todo voer RMIobject uit met deze agent oneindig lang doorheen het systeem van node naar node gaan
        } catch (Exception e) { e.printStackTrace(); }
    }
}