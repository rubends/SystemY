package be.ua;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import static be.ua.Main.controller;

public class View extends JFrame{

    //declared for making listeners
    private java.awt.Button logoutButton,openButton,removeButton,removeLocalButton;
    private java.awt.TextArea logText;
    private javax.swing.JScrollPane jScrollPane;

    public View()
    {
        JPanel panel = new JPanel();
        setSize(500, 330);

        //init components
        removeButton = new java.awt.Button();
        openButton = new java.awt.Button();
        removeLocalButton = new java.awt.Button();
        logoutButton = new java.awt.Button();
        logText = new java.awt.TextArea();
        jScrollPane = new JScrollPane(controller.getList());

        //add labels
        removeButton.setLabel("Remove File");
        openButton.setLabel("Open File");
        removeLocalButton.setLabel("Remove Local File");
        logoutButton.setLabel("EXIT");

        //GUI border
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Project: System Y - Node name: " + Main.nodeName));
        setName("SystemY"); // NOI18N

        //make layout attached to panel
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(logText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(19, 19, 19)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(removeLocalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(logoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(openButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(openButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(removeLocalButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(57, 57, 57)
                                                .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(logText, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        //add panel to the view
        add(panel);
    }

    //the listeners
    public void openButtonListener(ActionListener event)
    {
        this.openButton.addActionListener(event);
    }
    public void removeButtonListener(ActionListener event)
    {
        this.removeButton.addActionListener(event);
    }
    public void removeLocalButtonListener(ActionListener event) {this.removeLocalButton.addActionListener(event);}
    public void logoutButtonListener(ActionListener event) {this.logoutButton.addActionListener(event);}

    public void writeLogs(String logText){this.logText.append(printStamp() + logText + "\n");}
    public String printStamp(){
        LocalDateTime now = LocalDateTime.now();
        return "[" + ((now.getHour() < 10 ? "0" : "") + now.getHour()) + ":"
                + ((now.getMinute() < 10 ? "0" : "") + now.getMinute()) + ":"
                + ((now.getSecond() < 10 ? "0" : "") + now.getSecond()) + "]\t";
    }
}
