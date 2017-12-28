package be.ua;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static be.ua.Replication.fileMap;

public class UpdateFileMapThread extends Thread{

    //time between check + attributes to check if new file is added
    private static final int WAITTIME = 5;
    private List<File> newFileList = new ArrayList<>();
    private List<File> prevFileList = new ArrayList<>();

    //needed for replication
    NameServerInterface INameServer;
    String nodeName;

    //get filepath
    File fileLocDir = new File(Main.pathToLocalFiles);

    public UpdateFileMapThread(String nodeName, NameServerInterface INameServer) {
        this.INameServer = INameServer;
        this.nodeName = nodeName;
    }

    public void run() {
        while(true){

            //fill prev list
            prevFileList.clear();
            prevFileList.addAll(newFileList);

            //fill new list
            newFileList.clear();
            newFileList.addAll(Arrays.asList(fileLocDir.listFiles()));

            //compare lists
            try{
                if (!newFileList.equals(prevFileList) & prevFileList.size()>0){
                    System.out.println("Change in local/replications files is detected by UpdateFileMapThread.java" +
                            "\n Starting replication.");
                    Replication replication = new Replication(INameServer);
                    replication.setNodeName(nodeName);

                    if (newFileList.size() > prevFileList.size()){
                        newFileList.removeAll(prevFileList);

                        for (File file : newFileList) {
                            int ownHash = Main.INode.getId();
                            String ownIp = INameServer.getNodeIp(ownHash);
                            while(ownIp == null){
                                ownIp = INameServer.getNodeIp(ownHash);
                            }
                            fileMap.put(file.getName(), new FileMap(file.getName(),ownIp,ownHash));
                            replication.replicate(file.getName(), file.getAbsolutePath());
                        }
                        newFileList.clear();
                        prevFileList.clear();
                    }
                }
                TimeUnit.SECONDS.sleep(WAITTIME);
            }catch (Exception e){e.printStackTrace();}
        }
    }
}
