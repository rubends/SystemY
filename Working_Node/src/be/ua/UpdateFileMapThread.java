package be.ua;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UpdateFileMapThread extends Thread{
    
    private static final int WAITTIME = 3;
    public List<File> fileList = new ArrayList<>();
    public TreeMap<String, Boolean> nodeFileList = new TreeMap<>(); //filename + isOwner/ornot
    public TreeMap<String, Boolean> prevNodeFileList = new TreeMap<>();

    @Override
    public void run() {

        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator");

        File fileReplDir = new File(rootPath + sep + "Files" + sep + "Replication" + sep);
        File fileLocDir = new File(rootPath + sep + "Files" + sep + "Local" + sep);

        while(true){

            //fill previous node file list with keys of node file list.
            prevNodeFileList.clear();
            for (Map.Entry<String, Boolean> entry : nodeFileList.entrySet()) {
                prevNodeFileList.put(entry.getKey(), entry.getValue());
            }

            //get new content for node file list
            fillListWithFiles(fileReplDir, false);
            fillListWithFiles(fileLocDir, true);

            //Compare if previous is different than current node file list.
            List<String> currentKeys = new ArrayList<>();
            List<String> prevKeys = new ArrayList<>();
            if(prevNodeFileList.size() > 0) //first prev node file list will be different - because node list gets filld
            {
                //fill lists with content
                for (Map.Entry<String, Boolean> entry : nodeFileList.entrySet()) {
                    currentKeys.add(entry.getKey());
                }
                for (Map.Entry<String, Boolean> entry : prevNodeFileList.entrySet()) {
                    prevKeys.add(entry.getKey());
                }

                //check if there is a different in lists
                if (!currentKeys.equals(prevKeys)){
                    System.out.println("Change in local/replications files is detected by UpdateFileMapThread.java");

                    //TODO: Implement to give mynodefilelist to other class
                }
            }

            try{
                TimeUnit.SECONDS.sleep(WAITTIME);
            }catch(Exception e){
                System.out.println("Problem sleeping - updatefilemapthread");
                e.printStackTrace();
            }
        }
    }

    public void fillListWithFiles(File fileDir, boolean isOwner){
        fileList.clear();
        fileList.addAll(Arrays.asList(fileDir.listFiles()));
        while (nodeFileList.values().remove(isOwner));
        for (File f: fileList){
            if (f.isFile()){
                nodeFileList.put(f.getName(), isOwner);
            }
        }
    }
}
