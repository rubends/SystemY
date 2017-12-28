package be.ua;

import java.awt.*;
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
                    "\t2. Print local file list\n" +
                    "\t3. Print file fiches\n" +
                    "\t4. Open file \n" +
                    "\t5. Delete file \n" +
                    "\t6. Delete local file \n" +
                    "\t7. Node failure\n");
            System.out.println(" > ");

            int action = input.nextInt();
            try {
                if (action == 0) {
                    Main.INode.shutdown();
                } else if (action == 1) {
                    System.out.println("previous node: " + Main.INode.getPrevNode());
                    System.out.println("next node: " + Main.INode.getNextNode());
                } else if (action == 2) {
                    TreeMap<String, Boolean> localFileList = Main.INode.getLocalFileList();
                    if(localFileList.size() > 0) {
                        for (Map.Entry<String, Boolean> entry : localFileList.entrySet()) {
                            System.out.println("Name: " + entry.getKey() + ". Locked: " + entry.getValue());
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
                    openFile(file);
                } else if (action == 5) {
                    System.out.println("Give filename:");
                    String file = input.next();
                    deleteFile(file);
                } else if (action == 6) {
                    System.out.println("Give filename:");
                    String file = input.next();
                    deleteLocalFile(file);
                } else if (action == 7) {
                    System.out.println("Give failed node hash:");
                    int nodeHash = input.nextInt();
                    Main.INode.failure(nodeHash);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void openFile(String filename){
        try {
            String ip = INameServer.getFileIp(filename);
            String ownIp = INameServer.getNodeIp(Main.INode.getId());
            System.out.println("File is located at " + ip);
            if(ip.equals(ownIp)) { //file is on own system
                //node is zelf owner van het bestand, dus het kan geopend worden
                Desktop.getDesktop().open(getFile(filename).getAbsoluteFile());
            } else {
                //FileAgent.setLock(filename, true);

                INode fileNode = (INode) Naming.lookup( "//"+ip + "/" + INameServer.getHashOfIp(ip));
                String downloadIp = fileNode.getDownloadLocation(filename);
                System.out.println("download ip: " + downloadIp);

                INode downloadNode = (INode) Naming.lookup( "//"+downloadIp + "/" + INameServer.getHashOfIp(downloadIp));
                downloadNode.sendFile(ownIp, filename);

                fileNode.updateFiche(filename,Main.INode.getId(),ownIp);
                System.out.println("opening file " + filename);
                Desktop.getDesktop().open(getFile(filename).getAbsoluteFile());

                //FileAgent.setLock(filename, false);
            }
        } catch (Exception e){
            System.out.println("ERROR: file not found");
            e.printStackTrace();
        }
    }

    private void deleteFile(String filename){
        System.out.println("Deleting file " + filename);
        try {
            String ownIp = INameServer.getNodeIp(Main.INode.getId());
            String fileOwnerIp = INameServer.getFileIp(filename);
            if(fileOwnerIp.equals(ownIp)){ //file is on own system
                Replication.deleteFile(filename);
                Replication.fileMap.remove(filename);
            } else {
                INode fileNode = (INode) Naming.lookup( "//"+fileOwnerIp + "/" + INameServer.getHashOfIp(fileOwnerIp));
                fileNode.deleteFile(filename);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteLocalFile(String filename){
        System.out.println("Deleting local file " + filename);
        try {
            String fileOwnerIp = INameServer.getFileIp(filename);
            INode fileNode = (INode) Naming.lookup( "//"+fileOwnerIp + "/" + INameServer.getHashOfIp(fileOwnerIp));
            fileNode.deleteFileLocation(filename, Main.INode.getId());
            getFile(filename).delete();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static File getFile(String filename){
        File localFolder = new File(Main.pathToLocalFiles);
        File replicationFolder = new File(Main.pathToReplFiles);
        File[] localFiles = localFolder.listFiles();
        File[] replicationFiles = replicationFolder.listFiles();
        for(File file : localFiles){
            if(file.getName().equals(filename)){
                return file;
            }
        }
        for(File file : replicationFiles){
            if(file.getName().equals(filename)){
                return file;
            }
        }
        return null;
    }
}