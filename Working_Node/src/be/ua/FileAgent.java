package be.ua;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

public class FileAgent implements Runnable, Serializable {
    private TreeMap<String, Boolean> fileList;
    public FileAgent() { fileList = new TreeMap<>(); }

    private static final long serialVersionUID = 6529685098267757690L; // set the serializable class ID

    @Override
    public void run() {
        //adding local and replication files to Node.fileList
        for (String foldername: new String[]{"Local", "Replication"}) {
            File folder = new File(Main.pathToFiles+ foldername);
            addToList(folder.listFiles());
        }
        try {
            Main.INode.setLocalFileList(fileList);
        } catch (Exception e){
            e.printStackTrace();
        }
        //Node.localFileList = fileList;
        //Node.localFileList.putAll(fileList);
        //fileList.putAll(Node.localFileList);
    }

    public void addToList(File[] files){
        for (File file : files) {
            if(!fileList.containsKey(file.getName())){
                fileList.put(file.getName(), false);
            }
        }
    }

    public void setLock(String fileName, Boolean lock){
        if(fileList.containsKey(fileName)){
            fileList.remove(fileName);
        }
        fileList.put(fileName, lock);
        try {
            Main.INode.setLocalFileList(fileList);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
