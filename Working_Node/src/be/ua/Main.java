package be.ua;

public class Main {

    public static void main(String[] args) {
        MulticastThread multicastThread = new MulticastThread();
        multicastThread.start();
        System.out.println("nc: " + multicastThread.nodeCount);

        UserInterface ui = new UserInterface();
    }
}
