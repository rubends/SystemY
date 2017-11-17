package be.ua;

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

public class TCPReceiverThread extends Thread {

    public final static int SOCKET_PORT = 7896;
    public ServerSocket socket;

    public TCPReceiverThread() { }

    @Override
    public void run(){
        try{
            socket = new ServerSocket(SOCKET_PORT);
        }catch(IOException e){
            System.out.println("Exception in TCPReceiverThread:\n");
            e.printStackTrace();
        }

        while(true){

            try {
                Socket receivedSocket = socket.accept();
                InputStream is = receivedSocket.getInputStream();
                BufferedReader inFromSender = new BufferedReader(new InputStreamReader(is));
                String fileName = new DataInputStream(is).readUTF(); //get name from sender

                String rootPath = new File("").getAbsolutePath();
                String sep = System.getProperty("file.separator");
                File receivedFile = new File(rootPath + sep + "Files" + sep + "Replication"+ sep + fileName);
                FileOutputStream fos = new FileOutputStream(receivedFile);

                System.out.println("TCPReceiverThread: filename: " + fileName);
                DataOutputStream outToServer = new DataOutputStream(receivedSocket.getOutputStream());
                byte[] fileArray = Files.readAllBytes(receivedFile.toPath());
                outToServer.write(fileArray);
                System.out.println("FROM SERVER: " + inFromSender.readLine());

                receivedSocket.close();
                fos.close();
                is.close();

            }catch(IOException e){
            }

        }
    }
}