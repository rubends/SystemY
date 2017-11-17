package be.ua;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
                String fileName = new DataInputStream(is).readUTF(); //get name from sender
                String rootPath = new File("").getAbsolutePath();
                String sep = System.getProperty("file.separator");
                File receivedFile = new File(rootPath + sep + "Files" + sep + "Replication"+ sep + fileName);
                FileOutputStream fos = new FileOutputStream(receivedFile);
                int bufferLength = is.available();
                byte[] data = new byte[bufferLength];
                int length = is.read(data);
                fos.write(data, 0, length);

                System.out.println("TCPReceiverThread: filename: " + fileName);
                receivedSocket.close();
                fos.close();
                is.close();

            }catch(IOException e){
            }

        }
    }
}