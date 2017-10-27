package be.ua;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {
        Multicast multicast = new Multicast();
        multicast.Connect();
        if(args.length>0) {
            UserInterface ui = new UserInterface(args[0]);
        }
        else {
            //default
            UserInterface ui = new UserInterface("127.0.0.1");
        }
    }
}
