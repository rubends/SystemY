package be.ua;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.String;

public class UserInterface {
    private Scanner input;
    private NameServerInterface NameServerInterface;
    public  MulticastThread multicastThread;
    private INode INode;

    public UserInterface(String serverPort) {
        input = new Scanner(System.in);
        System.out.println("Startup of node");

        setup(serverPort);
        startUI();
    }

    private void setup(String serverPort) {
        RMIConnector connector =new RMIConnector(serverPort);
        NameServerInterface = connector.getNameServer();

    }

    public void startUI() {
        while(true) {
            //getAmountOfNodes();

            System.out.println("\t Enter file name then press 'Enter' to get the IP ");
            System.out.print("> ");

            try {
                String fileName = input.next();
                System.out.println("\t Input =" + fileName);
                AskServer(fileName);
            }

            catch (InputMismatchException e) {
                // clear buffer
                input.nextLine();
                System.out.println("Something went wrong!");
            }

            System.out.println("--\n");
        }
    }

    private void  AskServer(String fileName) {
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
    private void  getAmountOfNodes() {
        try{
            int passedNodesAmount= multicastThread.getAmountOfNodes();
            System.out.println("passedNodesAmount = " + passedNodesAmount);

            if(passedNodesAmount < 1){
                int nextNode = 0;
                int prevNode = 0;
            }
            else if(passedNodesAmount >= 1){
                //??
            }
        }
        catch(java.lang.NullPointerException np){
            System.out.println("//------------------------------------------------------------//");
            System.out.println("//Nullpointer op fetch van multicastThread.getAmountOfNodes() //");
            System.out.println("//------------------------------------------------------------//");
        }
    }
}