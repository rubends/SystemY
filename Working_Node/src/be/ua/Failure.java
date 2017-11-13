package be.ua;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Failure {
    private NameServerInterface NameServerInterface;
    private INode INode;
    private int prevID;
    private int nextID;
    public Failure(NameServerInterface NameServerInterface,INode INode)
    {
        this.INode = INode;
        this.NameServerInterface = NameServerInterface;
    }
    public void ActOnFailure(NameServerInterface NameServerInterface,INode INode)
    {

        try{//Ophalen vna de vorige en volgende node ID's //2 of failure
            ArrayList<Integer> ids = NameServerInterface.getNeighbourNodes(INode.getId());
            prevID = ids.get(0);
            nextID = ids.get(1);
        }
        catch(RemoteException e){}
        //3 of failure
        try{
            Registry registry = LocateRegistry.getRegistry(1098);
            INode = (INode) registry.lookup(Integer.toString(prevID));
            INode.updateNextNode(prevID);
        }
        catch(RemoteException e){System.out.println("RMI problem" ); }
        catch(NotBoundException e){System.out.println("Int TO String problem" );}

        //4 of failure
        try{
            Registry registry = LocateRegistry.getRegistry(1098);
            INode = (INode) registry.lookup(Integer.toString(nextID));
            INode.updatePrevNode(nextID);
        }
        catch(RemoteException e){System.out.println("RMI problem" ); }
        catch(NotBoundException e){System.out.println("Int TO String problem" );}

        //5 of failure = CALL SHUTDOWN SO IT GETS REMOVED FROM SERVER
        try{
            String nameServer = "nodeNames";
            Registry registry = LocateRegistry.getRegistry(1099);
            NameServerInterface = (NameServerInterface) registry.lookup(nameServer);
        }
        catch(RemoteException e){System.out.println("RMI problem" ); }
        catch(NotBoundException e){System.out.println("Int TO String problem" );}


        System.out.println("FAILURE SEQUENCE DONE" );

    }


}
