package be.ua;

import java.util.TreeMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileMap {
    private String filename;
    private HashMap<Integer, String> FileLocation = new HashMap<>();

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
}
