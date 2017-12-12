package be.ua;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.String;
import java.util.TreeMap;

public class UserInterface {
    private NameServerInterface INameServer;

    public UserInterface(NameServerInterface NameServerInterface) {
        INameServer = NameServerInterface;
        System.out.println("Startup of node");
    }

    public void startUI() {
        while(true) {
            Scanner input = new Scanner(System.in);
            System.out.println("\t __________________________________________ ");
            System.out.print("Chose Action:\n" +
                    "\t0. Shutdown\n" +
                    "\t1. Print neighbours\n" +
                    "\t2. Print system file list\n" +
                    "\t3. Print file fiches\n" +
                    "\t4. Test failure\n" +
                    "\t5. Open file: \n" +
                    "\t6. Delete file: \n");
            System.out.println(" > ");

            //@todo MORE TESTS!
            int action = input.nextInt();
            try {
                if (action == 0) {
                    shutdown();
                } else if (action == 1) {
                    ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(Node.nodeHash);
                    System.out.println("previous node: " + neighbours.get(0));
                    System.out.println("next node: " + neighbours.get(1));
                } // .................................
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void shutdown(){
        System.out.println("Shutting down node");
        try {
            ArrayList<Integer> neighbours = INameServer.getNeighbourNodes(Node.nodeHash);
            int prevHash = neighbours.get(0);
            int nextHash = neighbours.get(1);
            if(prevHash != Node.nodeHash){
                String prevIp = INameServer.getNodeIp(prevHash);
                INode prevNode = (INode) Naming.lookup("//"+prevIp+"/"+Integer.toString(prevHash));
                prevNode.updateNextNode(nextHash);
            }
            if(nextHash != Node.nodeHash)
            {
                String nextIp = INameServer.getNodeIp(nextHash);
                INode nextNode = (INode) Naming.lookup("//"+nextIp+"/"+Integer.toString(nextHash));
                nextNode.updatePrevNode(prevHash);
            }

            Replication replication = new Replication(INameServer);
            replication.toPrevNode(prevHash);

            INameServer.deleteNode(Node.nodeHash);
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}