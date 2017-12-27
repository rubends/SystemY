package be.ua;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static be.ua.Main.rmiAgent;

public class RMIAgent extends UnicastRemoteObject implements RMIAgentInterface,Runnable, Serializable {

    int nextNodeId;
    int myNodeId;
    NameServerInterface ns;
    FileAgent fileagent;
    public RMIAgent(NameServerInterface ns) throws RemoteException {
        nextNodeId = Main.INode.getNextNode();
        myNodeId = Main.INode.getId();
        this.ns = ns;
    }

    public void run(){
        FileAgent fileAgent = new FileAgent();
        RMIConnector connector = new RMIConnector();
        try {
            rmiAgent = new RMIAgent(connector.getNameServer());
            if(MulticastThread.nodeCount == 1) {
                connector.createRMIAgent(rmiAgent);
            }
            rmiAgent.passFileAgent(fileAgent);
        } catch (Exception e) {
            System.out.println("Error in RMIAgent.run");
            e.printStackTrace();
        }
    }

    public FileAgent startFileAgent(FileAgent fileAgent) {
        Thread at = new Thread(fileAgent);
        System.out.println("RMIAGENT.java: File agent started.");
        at.run();
        while(true) {
            if (!at.isAlive()) {
                return fileAgent;
            }
        }
    }

    public void passFileAgent(FileAgent fileAgent){
        try {
            System.out.println("entering pass file agent");
            nextNodeId = Main.INode.getNextNode();
            String ipNextNode = ns.getNodeIp(nextNodeId);
            System.out.println("RMI-AGENT: File agent passing to " + ipNextNode );
            RMIAgentInterface rmiAgent = (RMIAgentInterface) Naming.lookup( "//"+ipNextNode+":2000/RMIAgent");
            fileAgent.run();
            FileAgent passedAgent = rmiAgent.startFileAgent(fileAgent);
            Thread.sleep(5000);
            rmiAgent.passFileAgent(passedAgent);
            System.out.println("exit pass file agent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FailureAgent startFailureAgent(FailureAgent failureAgent) throws RemoteException {
        Thread at = new Thread(failureAgent);
        System.out.println("RMIAGENT.java: Failure agent started.");
        at.start();
        while(true) {
            if (!at.isAlive()) {
                return failureAgent;
            }
        }
    }

    public void passFailureAgent(FailureAgent failureAgent) throws RemoteException {
        try {
            if((nextNodeId != failureAgent.agentNode)){
                System.out.println("RMI-AGENT: Failure-agent passing to " + ns.getNodeIp(nextNodeId));
                RMIAgentInterface rmiAgent = (RMIAgentInterface) Naming.lookup( "//"+ ns.getNodeIp(nextNodeId)+"/RMIAgent");
                FailureAgent passedAgent = rmiAgent.startFailureAgent(failureAgent);
                rmiAgent.passFailureAgent(passedAgent);
            }
            else{
                System.out.println("RMI-AGENT: node failed: "+ failureAgent.failureNode +" verwijderd uit name server.");
                ns.deleteNode(failureAgent.failureNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "RMIAgent{" +
                "nextNodeId=" + nextNodeId +
                ", myNodeId=" + myNodeId +
                ", ns=" + ns +
                '}';
    }
}
