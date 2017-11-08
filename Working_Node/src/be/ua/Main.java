package be.ua;

public class Main {

    public static void main(String[] args) {
        MulticastThread multicastThread = new MulticastThread();
        multicastThread.start();
        UserInterface ui = new UserInterface();
    }

}
