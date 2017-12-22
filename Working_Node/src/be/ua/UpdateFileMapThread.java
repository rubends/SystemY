package be.ua;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static be.ua.Replication.fileMap;

public class UpdateFileMapThread extends Thread{

    public UpdateFileMapThread(String nodeName, NameServerInterface INameServer) {
        this.INameServer = INameServer;
        this.nodeName = nodeName;
    }

    private static final int WAITTIME = 3;
    public List<File> fileList = new ArrayList<>();
    public TreeMap<File, Boolean> nodeFileList = new TreeMap<>(); //filename + isOwner/ornot
    public TreeMap<File, Boolean> prevNodeFileList = new TreeMap<>();
    NameServerInterface INameServer;
    String nodeName;

    @Override
    public void run() {

        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator");
        File fileLocDir = new File(rootPath + sep + "Files" + sep + "Local" + sep);

        while(true){

            //fill previous node file list with keys of node file list.
            prevNodeFileList.clear();
            for (Map.Entry<File, Boolean> entry : nodeFileList.entrySet()) {
                prevNodeFileList.put(entry.getKey(), entry.getValue());
            }

            //get new content for node file list
            fillListWithFiles(fileLocDir, true);

            //Compare if previous is different than current node file list.
            List<File> currentKeys = new ArrayList<>();
            List<File> prevKeys = new ArrayList<>();
            if(prevNodeFileList.size() > 0) //first prev node file list will be different - because node list gets filld
            {
                //fill lists with content
                for (Map.Entry<File, Boolean> entry : nodeFileList.entrySet()) {
                    currentKeys.add(entry.getKey());
                }
                for (Map.Entry<File, Boolean> entry : prevNodeFileList.entrySet()) {
                    prevKeys.add(entry.getKey());
                }

                //check if there is a difference in the lists
                if (!currentKeys.equals(prevKeys)){
                    System.out.println("Change in local/replications files is detected by UpdateFileMapThread.java" +
                            "\n Starting replication.");

                    try{
                        Replication replication = new Replication(INameServer);
                        replication.setNodeName(nodeName);

                        //ADDED NEW FILE
                        if (currentKeys.size() > prevKeys.size()){
                            currentKeys.removeAll(prevKeys);

                            for (File file : currentKeys) {
                                int ownHash = Main.INode.getId();
                                fileMap.put(file.getName(), new FileMap(file.getName(),INameServer.getNodeIp(ownHash),ownHash));
                                replication.replicate(file.getName(), file.getAbsolutePath());
                            }
                        }
                    }
                    catch (Exception e){e.printStackTrace();}

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
        for (File file: fileList){
            if (file.isFile()){
                nodeFileList.put(file, isOwner);
            }
        }
    }

    @Override
    public String toString() {
        return "UpdateFileMapThread{" +
                "fileList=" + fileList +
                ", nodeFileList=" + nodeFileList +
                ", prevNodeFileList=" + prevNodeFileList +
                ", INameServer=" + INameServer +
                ", nodeName='" + nodeName + '\'' +
                '}';
    }
}
