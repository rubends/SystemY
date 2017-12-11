package be.ua;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

public class FileAgent implements Runnable, Serializable {

    private TreeMap<File, Boolean> fileList = new TreeMap<>();

    @Override
    public void run() {
        Node.fileList.clear();
        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator"); //OS dependable
        File localFolder = new File(rootPath + sep + "Files" + sep + "Local");
        File[] localFiles = localFolder.listFiles();
        File replicationFolder = new File(rootPath + sep + "Files" + sep + "Replication");
        File[] replicationFiles = replicationFolder.listFiles();
        addToList(localFiles);
        addToList(replicationFiles);
        Node.fileList = fileList;
        //@todo lock stuff
    }

    public void addToList(File[] files){
        for (int i = 0; i < files.length; i++) {
            if(!fileList.containsKey(files[i])){
                fileList.put(files[i], true);
            }
        }
    }
}
