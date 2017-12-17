package be.ua;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.util.TreeMap;

public class View extends JFrame{
    FileAgent fileAgent;
    Controller Controller;
    JLabel label3 = new JLabel("        Swing GUI       ");
    private JButton openButton, removeButton, downloadButton,logoutButton;

    public View(FileAgent fileAgent)
    {
        this.fileAgent = fileAgent;
        this.setBackground(Color.DARK_GRAY);
        this.setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        JPanel panel = new JPanel();
        JScrollPane pane = new JScrollPane(Controller.getList());
        //pane.createVerticalScrollBar();
        openButton = new JButton("Open File");
        removeButton = new JButton("Remove File");
        downloadButton = new JButton("Download File");
        logoutButton = new JButton("Logout");

        add(label3, BorderLayout.CENTER);
        add(openButton, BorderLayout.WEST);
        add(removeButton, BorderLayout.EAST);
        add(logoutButton, BorderLayout.SOUTH);
        add(pane, BorderLayout.CENTER);
    }
    //--------------------------------------------------------
    //Onderstaande functies worden gebruikt in de controller
    //--------------------------------------------------------
    public void openButtonListener(ActionListener event)
    {
        this.openButton.addActionListener(event);
    }

    public void removeButtonListener(ActionListener event)
    {
        this.removeButton.addActionListener(event);
    }

    public void downloadButtonListener(ActionListener event)
    {
        this.downloadButton.addActionListener(event);
    }
    //--------------------------------------------------------

}
