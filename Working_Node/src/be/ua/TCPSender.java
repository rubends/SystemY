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
            e.printStackTrace();
        }
    }

    public void downloadRequest(String filename, String ip) {
        File tmpFile = UserInterface.getFile(filename);
        String tmpPath = tmpFile.getAbsolutePath();
        try {
            SendFile(ip, tmpPath);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
