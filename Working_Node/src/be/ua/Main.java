package be.ua;

public class Main {

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        MulticastThread multicastThread = new MulticastThread(ui);
        multicastThread.start();
        ui.startUI();
    }

}
