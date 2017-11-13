package be.ua;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

public class TCPReceiver {

    public TCPReceiver(String[] args) throws Exception {
        int serverPort = 7896;
        String host = "localhost";
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a file: ");
        System.out.flush();
        String filename = scanner.nextLine();
        File file = new File(filename);

        Socket clientSocket = new Socket(host, serverPort);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        byte[] fileArray = Files.readAllBytes(file.toPath());
        outToServer.write(fileArray);
        System.out.println("FROM SERVER: " + inFromServer.readLine());
        clientSocket.close();
    }


}
