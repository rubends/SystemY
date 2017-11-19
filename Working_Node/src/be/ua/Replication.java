package be.ua;

import java.io.File;

public class Replication {
    NameServerInterface INameServer;
    File[] localFiles;

    public Replication(NameServerInterface ns) {
        INameServer = ns;
        getFiles();
    }

    public void getFiles() {
        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator");
        File folder = new File(rootPath + sep + "Files" + sep + "Local");
        localFiles = folder.listFiles();
        for (int i = 0; i < localFiles.length; i++) {
            //!!!!!!!!!!!!!!!!!!!!! fotos werken nog niet
            replicate(localFiles[i].getName(), localFiles[i].getAbsolutePath());
        }
    }

    public void replicate(String filename, String location){
        System.out.println(filename);
        try {
            String ip = INameServer.getFileIp(filename);
            TCPSender tcpSender = new TCPSender(7896);
            //!!!!!!!!!!!!!!!!!!!!!! if location is zichzelf --> naar de vorige node
            tcpSender.SendFile(ip, location);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
