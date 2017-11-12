package be.ua;

import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.String;

public class UserInterface {
    private Scanner input;
    private NameServerInterface NameServerInterface;

    public UserInterface() {
        input = new Scanner(System.in);
        System.out.println("Startup of node");
        setup();
    }

    private void setup() {
        RMIConnector connector = new RMIConnector();
        NameServerInterface = connector.getNameServer();
    }

    public void startMulticast(){
        System.out.println("\t Enter the name for the new node");
        System.out.print("> ");
        String nodeName = new Scanner(System.in).next();

        MulticastThread multicastThread = new MulticastThread(nodeName, NameServerInterface);
        multicastThread.start();
    }

    public void startUI() {
        while(true) {
            System.out.println("\t Enter file name then press 'Enter' to get the IP ");
            System.out.print("> ");

            try {
                String fileName = input.next();
                System.out.println("\t Input =" + fileName);
                askServer(fileName);
            }

            catch (InputMismatchException e) {
                // clear buffer
                input.nextLine();
                System.out.println("Something went wrong!");
            }

            System.out.println("--\n");
        }
    }

    private void  askServer(String fileName) {
        try {
            String Ipa = NameServerInterface.getFileIp(fileName);
            System.out.println("------------------------");
            System.out.println("fileName '" + fileName + "' at IP "+Ipa);
        }

        catch (Exception e) {
            // clear buffer
            input.nextLine();
            System.out.println("No good connection");
        }
    }

    protected void getNodeCount(){
        try{
            int nodeCount = NameServerInterface.getNodeCount();
            System.out.println("Nodecount: " + nodeCount);
        }
        catch(RemoteException e){
            e.printStackTrace();
        }


    }
}