package be.ua;

import java.io.File;
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
                    "\t2. Print local file list\n" +
                    "\t3. Print file fiches\n" +
                    "\t4. Open file \n" +
                    "\t5. Delete file \n" +
                    "\t6. Node failure\n");
            System.out.println(" > ");

            //@todo MORE TESTS!
            int action = input.nextInt();
            try {
                if (action == 0) {
                    Main.INode.shutdown();
                } else if (action == 1) {
                    System.out.println("previous node: " + Main.INode.getPrevNode());
                    System.out.println("next node: " + Main.INode.getNextNode());
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
                            System.out.println("File: " + entry.getKey() + "/" + INameServer.getHashOfName(entry.getKey()) + " on location: " + entry.getValue().getIpOfLocation() + " with hash " + entry.getValue().getHashOfLocation());
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
                    Main.INode.failure(nodeHash);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}