package be.ua;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.*;

public class Controller implements Observer{
    View view;
    FileAgent fileAgent;
    private TreeMap<String, Boolean> fileList;
    private DefaultListModel listModel = new DefaultListModel();
    private static JList list;
    UserInterface ui = new UserInterface(Main.NameServerInterface);

    Controller(FileAgent fileAgent){
        this.fileAgent = fileAgent;
        this.list = new JList(listModel);
        this.fileList = new TreeMap<String,Boolean>();
        fillListModel();
    }

    //setters & getters
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


    //Initialize listeners
    public void createListeners(View view_){
        view = view_;
        view.openButtonListener(new openSelectionListener());
        view.removeButtonListener(new removeSelectionListener());
        view.removeLocalButtonListener(new removeLocalSelectionListener());
        view.logoutButtonListener(new logoutSelectionListener());
    }

    //Every listener:
    class openSelectionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ui.openFile(list.getSelectedValue().toString());
        }
    }
    class removeSelectionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ui.deleteFile(list.getSelectedValue().toString());
        }
    }
    class logoutSelectionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            try {
                Main.INode.shutdown();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }
    class removeLocalSelectionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ui.deleteLocalFile(list.getSelectedValue().toString());
        }
    }


    //observer pattern
    public void update(Observable o, Object arg){
        refreshListModel();
    }

    public void refreshListModel()
    {
        emptyListModel();
        fillListModel();
        view.writeLogs("Refreshed list.");
        setList(new JList(listModel));
        //todo use listmodel?
    }

    public void emptyListModel()
    {
        listModel.removeAllElements();
    }

    public void fillListModel()
    {
        try{
        fileList = Main.INode.getLocalFileList();
        }catch(Exception e){e.printStackTrace();}

        if(fileList!=null){
            //System.out.println("GUI: filelist" + fileList);
            for(Map.Entry<String, Boolean> entry : fileList.entrySet())
            {
                //System.out.println("GUI: Key: "+entry.getKey()+". Value: " + entry.getValue());
                listModel.addElement(entry.getKey());
            }
        }
        else{
            System.out.println("Nothing in the file list");
        }
        System.out.println(listModel);
    }
}
