package be.ua;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

public class FileAgent implements Runnable, Serializable {
    private static TreeMap<String, Boolean> fileList;
    public FileAgent() { fileList = new TreeMap<>(); }

    @Override
    public void run() {

        Node.localFileList.clear();
        //adding local and replication files to Node.fileList
        for (String foldername: new String[]{"Local", "Replication"}) {
            File folder = new File(Main.pathToFiles+ foldername);
            addToList(folder.listFiles());
        }
        Node.localFileList = fileList;

        //debugline: check filelist size
        System.out.println("file agent ran on node, here is filelist size: " + fileList.size());
    }

    public void addToList(File[] files){
        for (File file : files) {
            if(!fileList.containsKey(file.getName())){
                fileList.put(file.getName(), false);
            }
        }
    }

    public static void setLock(String fileName, Boolean lock){
        if(fileList.containsKey(fileName)){
            fileList.remove(fileName);
        }
        fileList.put(fileName, lock);
    }
}
