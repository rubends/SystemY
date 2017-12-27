package be.ua;

import java.io.File;
import java.io.Serializable;

public class FileAgent implements Runnable, Serializable {
    public FileAgent() { }

    @Override
    public void run() {

        //adding local and replication files to Node.fileList
        for (String foldername: new String[]{"Local", "Replication"}) {
            File folder = new File(Main.pathToFiles+ foldername);
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

    public static void setLock(String fileName, Boolean lock){
        File file = UserInterface.getFile(fileName);
        Node.fileList.remove(file);
        Node.fileList.put(file, lock);
    }
}
