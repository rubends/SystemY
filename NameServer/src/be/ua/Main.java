package be.ua;

public class Main {

    public static void main(String[] args) {
        try {
            NameServerInterface ns = new NameServer();
            RMIConnector rmi = new RMIConnector(ns);
            MulticastThread multicastThread = new MulticastThread(ns);
            multicastThread.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
