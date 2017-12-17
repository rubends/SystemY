package be.ua;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

public class Controller implements Observer{
    View view;
    FileAgent fileAgent;
    private TreeMap<String, Boolean> fileList;
    private DefaultListModel listModel = new DefaultListModel();
    private static JList list;

    Controller(FileAgent fileAgent){
        this.fileAgent = fileAgent;
        this.list = new JList(listModel);
        this.fileList = new TreeMap<String,Boolean>();
        fillListModel();
    }
    public void createListeners(View view_){
        view = view_;
        view.openButtonListener(new openSelectionListener());
        view.removeButtonListener(new removeSelectionListener());
        view.downloadButtonListener(new downloadSelectionListener());
    }

    class openSelectionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Open selected: "+ list.getSelectedValue());
        }
    }
    class removeSelectionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Remove selected: "+ list.getSelectedValue());
        }
    }

    class downloadSelectionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Download selected: " + list.getSelectedValue());
        }
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
        System.out.println("list = " + list);
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
}
