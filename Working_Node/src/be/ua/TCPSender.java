package be.ua;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;

public class TCPSender {
    public int Port;
    public TCPSender(int portNumber){
        Port = portNumber;
    }

    public void SendFile(String ip, String filename) throws IOException{
        try {
            File file = new File(filename);
            Socket socket = new Socket(InetAddress.getByName(ip), Port);
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            byte[] fileArray = Files.readAllBytes(file.toPath());
            outToServer.writeUTF(file.getName());
            outToServer.write(fileArray);
            socket.close();
        } catch (Exception e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    public void downloadRequest(String filename, String ip) {
        File tmpFile = getFile(filename);
        String tmpPath = tmpFile.getAbsolutePath();
        try {
            SendFile(ip, tmpPath);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private File getFile(String filename){
        String rootPath = new File("").getAbsolutePath();
        String sep = System.getProperty("file.separator");
        File localFolder = new File(rootPath + sep + "Files" + sep + "Local");
        File replicationFolder = new File(rootPath + sep + "Files" + sep + "Replication");
        File[] localFiles = localFolder.listFiles();
        File[] replicationFiles = replicationFolder.listFiles();
        for(File file : localFiles){
            if(file.getName().equals(filename)){
                return file;
            }
        }
        for(File file : replicationFiles){
            if(file.getName().equals(filename)){
                return file;
            }
        }
        return null;
    }
}
