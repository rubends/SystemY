package be.ua;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileMap implements Serializable {
    private String filename;
    public HashMap<Integer, String> FileLocation = new HashMap<>();

    FileMap(String filename_, String localNodeip_, int localNodehash_){
        filename = filename_;
        addLocation(localNodeip_,localNodehash_);
    }
    public String getFilename() {
        return filename;
    }

    public void addLocation(String ip,int hash){
        FileLocation.put(hash,ip);
    }
    public int getHashOfLocation() {
        Iterator<Integer> keySetIterator = FileLocation.keySet().iterator();
        while (keySetIterator.hasNext()) {
            int key = keySetIterator.next();
            //System.out.println("hash of location = " + key);
            return key;
        }
        return 0;
    }
    public String getIpOfLocation() {
        Iterator it = FileLocation.entrySet().iterator();
        //todo random location
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String location = (String)pair.getValue();
            //System.out.println("IP of location = " + location);
            return location;
        }
        return null;
    }

    public void printLocation(){
        Iterator<Integer> keySetIterator = FileLocation.keySet().iterator();
        while (keySetIterator.hasNext()) {
            int key = keySetIterator.next();
            //System.out.println(key + " - " + FileLocation.get(key));
        }
    }
}
