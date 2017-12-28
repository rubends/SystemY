package be.ua;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIAgent extends UnicastRemoteObject implements RMIAgentInterface {

    NameServerInterface ns;

    public RMIAgent(NameServerInterface ns) throws RemoteException {
        this.ns = ns;
    }

    public void startFileAgent(FileAgent fileAgent) {
        Thread at = new Thread(fileAgent);
        at.start();
        //System.out.println("RMIAGENT.java: File agent started.");
        try {
            at.join();
            Thread.sleep(3000);
            passFileAgent(fileAgent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void passFileAgent(FileAgent fileAgent){
        try {
            int nextNodeId = Main.INode.getNextNode();
            String ipNextNode = ns.getNodeIp(nextNodeId);
            if(!ipNextNode.equals(ns.getNodeIp(Main.INode.getId()))){
                System.out.println("RMI-AGENT: File agent passing to " + ipNextNode );
                RMIAgentInterface rmiAgent = (RMIAgentInterface) Naming.lookup( "//"+ipNextNode+":1101/RMIAgent");
                rmiAgent.startFileAgent(fileAgent);
            }
        } catch (Exception e) {
            try{
                Thread.sleep(2500);
                int nextNodeId = ns.getNeighbourNodes(Main.INode.getId()).get(1);
                String ipNextNode = ns.getNodeIp(nextNodeId);
                if(!ipNextNode.equals(ns.getNodeIp(Main.INode.getId()))){
                    System.out.println("RMI-AGENT: File agent passing to " + ipNextNode );
                    RMIAgentInterface rmiAgent = (RMIAgentInterface) Naming.lookup( "//"+ipNextNode+":1101/RMIAgent");
                    rmiAgent.startFileAgent(fileAgent);
                }
            }catch (Exception ex){}

            e.printStackTrace();
        }
    }

    public void startFailureAgent(FailureAgent failureAgent) throws RemoteException {
        Thread at = new Thread(failureAgent);
        at.start();
        System.out.println("RMIAGENT.java: Failure agent started.");
        try {
            at.join();
            passFailureAgent(failureAgent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void passFailureAgent(FailureAgent failureAgent) throws RemoteException {
        try {
            int nextNodeId = Main.INode.getNextNode();
            if((nextNodeId != failureAgent.agentNode)){
                System.out.println("RMI-AGENT: Failure-agent passing to " + ns.getNodeIp(nextNodeId));
                RMIAgentInterface rmiAgent = (RMIAgentInterface) Naming.lookup( "//"+ ns.getNodeIp(nextNodeId)+":1101/RMIAgent");
                rmiAgent.startFailureAgent(failureAgent);
            }
            else{
                System.out.println("RMI-AGENT: node failed: "+ failureAgent.failureNode +" verwijderd uit name server.");
                //ns.deleteNode(failureAgent.failureNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
