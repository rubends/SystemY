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

    public void SendFile(File file, String ip) throws IOException{
        Socket socket = new Socket(InetAddress.getByName(ip), Port);
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        byte[] fileArray = Files.readAllBytes(file.toPath());
        outToServer.write(fileArray);
        socket.close();
    }
}
