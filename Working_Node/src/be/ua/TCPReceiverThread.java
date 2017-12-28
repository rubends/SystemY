package be.ua;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceiverThread extends Thread {

    private final static int SOCKET_PORT = 7897;//7897
    private ServerSocket socket;

    public TCPReceiverThread() { }

    @Override
    public void run(){
        try{
            socket = new ServerSocket(SOCKET_PORT);
            socket.setReuseAddress(true);
        }catch(IOException e){
            System.out.println("Could not connect to TCP port:\n");
            e.printStackTrace();
        }

        while(true){

            try {
                Socket receivedSocket = socket.accept();
                InputStream is = receivedSocket.getInputStream();
                DataInputStream inputStream = new DataInputStream(is);
                String fileName = inputStream.readUTF(); //get name from sender
                long fileLength = inputStream.readLong();
                File receivedFile = new File(Main.pathToReplFiles + fileName);
                FileOutputStream fos = new FileOutputStream(receivedFile);
                int bufferLength = is.available();
                int received = 0;
                while(fileLength > received){ //max size TCP packet is 64kb
                    if(bufferLength > 0) {
                        byte[] data = new byte[bufferLength];
                        int length = is.read(data);
                        fos.write(data, 0, length);
                        received = received + length;
                    } else {
                        break;
                    }
                }
                System.out.println("TCPReceiverThread: filename: " + fileName);
                receivedSocket.close();
                fos.close();
                is.close();

            }catch(Exception e){
            }
        }
    }
}