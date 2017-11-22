package be.ua;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        UpdateFileMapThread t = new UpdateFileMapThread();
        t.start();


        /*
        RMIConnector connector = new RMIConnector();
        NameServerInterface NameServerInterface = connector.getNameServer();

        UserInterface ui = new UserInterface(NameServerInterface);

        System.out.println("\t Enter the name for the new node");
        System.out.print("> ");
        String nodeName = new Scanner(System.in).next();
        MulticastThread multicastThread = new MulticastThread(nodeName, NameServerInterface);
        multicastThread.start();
        try{
            int hash = NameServerInterface.getHashOfName("Sam");
            NameServerInterface.getNode(hash);
        }
        catch(Exception e){}


        TCPReceiverThread tcpReceiverThread = new TCPReceiverThread();
        tcpReceiverThread.start();

        Replication replication = new Replication(NameServerInterface);
        */
    }
}

