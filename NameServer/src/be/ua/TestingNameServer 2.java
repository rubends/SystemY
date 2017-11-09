package be.ua;
import org.junit.jupiter.api.Test;


public class TestingNameServer {

    @Test
    void mappingTest(){

        //Voeg een node toe met een unieke naam
        //Probeer een node toe te voegen met een naam die reeds bestaat
        //Verwijder een node uit de Map
        //Probeer een node te verwijderen, die niet bestaat, uit de Map

        try{
            NameServerInterface ns = new NameServer();
            ns.addNode("node nr 1","192.168.1.1");
            ns.addNode("node nr 1","192.168.1.1");
            ns.addNode("node nr 2","192.168.1.2");
            ns.addNode("myfile","192.168.1.3");
            ns.printNodeMap();
            ns.deleteNode("node nr 2");
            ns.deleteNode("node nr 3");

            ns.getFileIp("myfile.jpg");
            ns.printNodeMap();

            System.out.println(ns.getFirstId());
            System.out.println(ns.getLastId());
        }
        catch (Exception e){}
    }


}