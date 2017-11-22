package be.ua;

import java.util.TreeMap;

public class FileMap {
    /*
        Om een onderscheid te maken tussen lokaal en gerepliceerd worden er twee maps gemaakt
     */
    private TreeMap<String, Integer> FileLocationLocal = new TreeMap<>();
    private TreeMap<String, Integer> FileLocationRepli = new TreeMap<>();
    FileMap(){
        /*
            In deze klasse worden alle locaties van bestanden bijgehouden
         */
    }
    public int getLocationLocal(String fileName) {
        return FileLocationLocal.get(fileName);
    }
    public void addLocationLocal(String fileName, int hashLocation) {
        /*
            De fileName is de naam van het bestand
            De hashLocation is het IP adres van waar het bestand zich bevind
         */
        FileLocationLocal.put(fileName,hashLocation);
    }
    public int getLocationRepli(String fileName) {
        return FileLocationRepli.get(fileName);
    }
    public void addLocationRepli(String fileName, int hashLocation) {
        /*
            De fileName is de naam van het bestand
            De hashLocation is het IP adres van waar het bestand zich bevind
         */
        FileLocationRepli.put(fileName,hashLocation);
    }
    public void passFiche(String fileName,int hashLocation,boolean flag) {
        //Moet nog verder gedaan worden
        //Hiervoor RMI connectie opzetten, dan zie hieronder (nog niet af!)
        //Voor in node method aanmaken die fiche zal verzenden, functie oproepen. Hieronder .put

        if(flag == true){
            addLocationLocal(fileName,hashLocation);
        }
        else{
            addLocationRepli(fileName,hashLocation);
        }

    }
}
