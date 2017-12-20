package be.ua;

public class Main {

    public static void main(String[] args) {
        try {
            NameServerInterface ns = new NameServer();
            RMIConnector rmi = new RMIConnector(ns);
            MulticastThread multicastThread = new MulticastThread(ns);
            multicastThread.start();
/*
            ns.addNode("node nr 1","192.168.1.1");
            ns.addNode("node nr 1","192.168.1.1");
            ns.addNode("node nr 2","192.168.1.20");
            ns.addNode("myfile","192.168.1.3");
            ns.printNodeMap();

            ns.deleteNode(ns.getHashOfName("node nr 2"));
            ns.deleteNode(ns.getHashOfName("node nr 3"));

            ns.getFileIp("myfile.jpg");
            ns.printNodeMap();  */

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
