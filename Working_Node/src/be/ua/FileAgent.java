package be.ua;

import java.io.File;
import java.io.Serializable;

public class FileAgent implements Runnable, Serializable {
    public FileAgent() { }
    String rootPath = new File("").getAbsolutePath();
    String sep = System.getProperty("file.separator"); //OS dependable
    String pathToFiles = rootPath + sep + "Files" + sep;

    @Override
    public void run() {

        //adding local and replication files to Node.fileList
        for (String foldername: new String[]{"Local", "Replication"}) {
            File folder = new File(pathToFiles + foldername);
            addToList(folder.listFiles());
        }

        //debugline: check filelist size
        System.out.println("file agent ran on node, here is filelist size: " + Node.fileList.size());
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
