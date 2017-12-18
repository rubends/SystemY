package be.ua;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIAgent extends UnicastRemoteObject implements RMIAgentInterface{

    int nextNodeId;
    NameServerInterface ns;

    public RMIAgent(INode iNode, NameServerInterface ns) throws RemoteException {
        nextNodeId = iNode.getNextNode();
        this.ns = ns;
    }

    public FileAgent startFileAgent(FileAgent fileAgent) {
        Thread at = new Thread(fileAgent);
        System.out.println("RMIAGENT.java: File agent started.");
        at.start();
        while(true) {
            if (!at.isAlive()) {
                return fileAgent;
            }
        }
    }

    public void passFileAgent(FileAgent fileAgent){
        try {
            System.out.println("RMI-AGENT: File agent passing to " + ns.getNodeIp(nextNodeId));
            RMIAgentInterface rmiAgent = (RMIAgentInterface) Naming.lookup( "//"+ ns.getNodeIp(nextNodeId)+"/RMIAgent");
            FileAgent passedAgent = rmiAgent.startFileAgent(fileAgent);
            Thread.sleep(5000);
            rmiAgent.passFileAgent(passedAgent);
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
                RMIAgentInterface rmiAgent = (RMIAgentInterface) Naming.lookup( "//"+ ns.getNodeIp(nextNodeId)+"/AgentRMI");
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
}
