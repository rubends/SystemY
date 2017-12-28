package be.ua;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.Observable;

public class FileAgent extends Observable implements Runnable, Serializable {

    private TreeMap<String, Boolean> fileList = new TreeMap<>();
    public FileAgent ()   {
    }

    public void setFileList(TreeMap<String, Boolean> fileList) {
        this.fileList = fileList;
        setChanged();
        notifyObservers();
    }

    @Override
    public void run() {
        UserInterface.fileList.clear();
        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator"); //OS dependable
        File localFolder = new File(rootPath + sep + "Files" + sep + "Local");
        File[] localFiles = localFolder.listFiles();
        File replicationFolder = new File(rootPath + sep + "Files" + sep + "Replication");
        File[] replicationFiles = replicationFolder.listFiles();
        addToList(localFiles);
        addToList(replicationFiles);
        UserInterface.fileList = fileList;
    }

    public void addToList(File[] files){
        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            if(!fileList.containsKey(filename)){
                fileList.put(filename, true);
                System.out.println("adding to file list: " + filename);
            }
        }
    }

    public TreeMap<String, Boolean> getFileList()
    {
        /*
        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator"); //OS dependable
        File localFolder = new File(rootPath + sep + "Files" + sep + "Local");
        File[] localFiles = localFolder.listFiles();
        File replicationFolder = new File(rootPath + sep + "Files" + sep + "Replication");
        File[] replicationFiles = replicationFolder.listFiles();
        addToList(localFiles);
        addToList(replicationFiles);
        */
        //-----
        //Al het bovenstaande moet niet, is voor testen van de gui

        //setFileList(fileList);
        return fileList;
    }


}
