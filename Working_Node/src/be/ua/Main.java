package be.ua;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.startMulticast();
        //ui.startUI();
        TCPReceiverThread tcpReceiverThread = new TCPReceiverThread();
        tcpReceiverThread.start();

        TCPSender tcpSender = new TCPSender(7896);
        try {
            System.out.print("Enter a file: "); //D:\Ruben\Documents\EI\5-DistributedSystems\SystemY\Working_Node\Files\Local\test.txt
            System.out.print("> ");
            String filename = new Scanner(System.in).nextLine();
            tcpSender.SendFile("192.168.56.1", filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
