package SP;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.String;

public class UserInterface {
    private Scanner input;
    private Inode Inode;

    public UserInterface(String serverPort) {
        input = new Scanner(System.in);
        System.out.println("Startup of node");
        setup(serverPort);
        System.out.println("Hier");
        startUI();
    }

    private void setup(String serverPort) {
        RMIConnector connector =new RMIConnector(serverPort);
        Inode = connector.getNameServer();
    }

    public void startUI() {
        while(true) {
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
            String Ipa = Inode.getFileIp(fileName);
            System.out.println("------------------------");
            System.out.println("fileName '" + fileName + "' at IP "+Ipa);
        }

        catch (Exception e) {
            // clear buffer
            input.nextLine();
            System.out.println("Wrong Input");
        }
    }
}