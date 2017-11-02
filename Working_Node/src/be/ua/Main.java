package be.ua;

public class Main {

    public static void main(String[] args) {
        MulticastThread multicastThread = new MulticastThread();
        multicastThread.start();
        if(args.length>0) {
            UserInterface ui = new UserInterface(args[0]);
        }
        else {
            //default
            UserInterface ui = new UserInterface("127.0.0.1");
        }
    }
}
