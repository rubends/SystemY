package be.ua;

import java.io.File;
import java.io.Serializable;

public class FileAgent implements Runnable, Serializable {
    public FileAgent() {
    }

    @Override
    public void run() {
        System.out.println("INSIDE FILEAGENT");
        Node.fileList.clear();
        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator"); //OS dependable
        File localFolder = new File(rootPath + sep + "Files" + sep + "Local");
        File[] localFiles = localFolder.listFiles();
        File replicationFolder = new File(rootPath + sep + "Files" + sep + "Replication");
        File[] replicationFiles = replicationFolder.listFiles();
        addToList(localFiles);
        addToList(replicationFiles);
    }

    public void addToList(File[] files){
        for (int i = 0; i < files.length; i++) {
            if(!Node.fileList.containsKey(files[i])){
                Node.fileList.put(files[i], false);
            }
        }
    }

    public void lockFile(File file){
        for (int i = 0; i < Node.fileList.size(); i++) {
            if(Node.fileList.containsKey(file)){
                Node.fileList.put(file, true);
            }
        }
    }
}
