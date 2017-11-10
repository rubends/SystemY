package be.ua;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("\t Enter the name for the new node");
        System.out.print("> ");
        String nodeName = new Scanner(System.in).next();

        MulticastThread multicastThread = new MulticastThread(nodeName);
        multicastThread.start();
        UserInterface ui = new UserInterface();
    }
}
