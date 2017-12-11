package be.ua;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class RMIAgent extends UnicastRemoteObject {
    public RMIAgent() throws RemoteException {
    }

    //Maak per node een RMI object waarin een methode staat die:
    public FileAgent startFileAgent(FileAgent fileAgent) {     //Als parameter een Agent mee krijgt
        Thread at = new Thread(fileAgent); //Een Thread maakt met de Agent als argument
        System.out.println("RMIAGENT.java: File agent started.");
        at.start(); //De Thread start.
        while(true) { //Wacht tot de thread stop.
            if (!at.isAlive()) {


                //todo : update
                return fileAgent;
            }
        }
    }

    //Dezelfde methode op de volgende node uitvoert (Tenzij de agent moet stoppen, cfr 2.iii)
    public void passFileAgent(FileAgent fileAgent){
        int nextNodeId = 0;
    }




}
