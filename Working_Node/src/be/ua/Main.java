package be.ua;

public class Main {

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.startMulticast();
        ui.startUI();
    }
}
