package be.ua;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

public class Controller implements Observer{
    View view;
    FileAgent fileAgent;
    private TreeMap<String, Boolean> fileList;
    private DefaultListModel listModel = new DefaultListModel();
    public static JList list;

    Controller(FileAgent fileAgent){
        this.fileAgent = fileAgent;
        this.list = new JList(listModel);
        this.fileList = new TreeMap<String,Boolean>();
        fillListModel();
    }


    public void createListeners(View view_){
        view = view_;
    }



    public void update(Observable o, Object arg){
        refreshListModel();
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }

    public static JList getList() {
        return list;
    }

    public void setList(JList list) {
        this.list = list;
    }

    public void fillListModel()
    {

        fileList = FileAgent.getFileList();
        if(fileList!=null){
            System.out.println("GUI: filelist" + fileList);
            for(Map.Entry<String, Boolean> entry : fileList.entrySet())
            {
                System.out.println("GUI: Key: "+entry.getKey()+". Value: " + entry.getValue());
                listModel.addElement(entry.getKey());

                //newList.add(entry.getKey());
            }
        }
        else{
            System.out.println("Nothing in the file list");
        }

    }

    public void emptyListModel()
    {
        listModel.removeAllElements();
    }

    public void refreshListModel()
    {
        emptyListModel();
        fillListModel();
    }

    //Actions when buttons are clicked
    public void openButtonClicked(String filename){
        System.out.println("open file: " + filename);
    }
    public void removeButtonClicked(String filename){
        System.out.println("remove file: " + filename);
    }
    public void removeLocalButtonClicked(String filename){
        System.out.println("remove local file: " + filename);
    }
    public void downloadButtonClicked(String filename){
        System.out.println("download file: " + filename);
    }

    public void logoutButtonClicked(){
        System.out.println("--");
        System.out.println(getList());
        System.out.println(getListModel());
    }
}
