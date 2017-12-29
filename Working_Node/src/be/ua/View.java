package be.ua;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import static be.ua.Main.controller;

public class View extends JFrame{

    //var
    public String ipNameserver = "";
    public String nodename = "";
    public String getIpNameserver() {
        return ipNameserver;
    }

    public void setIpNameserver(String ipNameserver) {
        this.ipNameserver = ipNameserver;
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename;
    }


    //declared for making listeners
    private java.awt.Button logoutButton,openButton,removeButton,removeLocalButton, loginButton;
    private java.awt.TextArea logText;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextField nodenameTextField, nameserverTextField;
    private javax.swing.JLabel nodenameLabel, nameserverLabel;

    public View(boolean firstLogin)
    {
        setTitle("System Y");
        setResizable(false);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        //init components - panel 1
        loginButton = new java.awt.Button();
        nodenameTextField = new javax.swing.JTextField();
        nameserverTextField = new javax.swing.JTextField();
        nodenameLabel = new javax.swing.JLabel();
        nameserverLabel = new javax.swing.JLabel();
        nodenameTextField.setText("");
        nameserverTextField.setText(Main.recommendedNameServer);
        nodenameLabel.setText("nodename:");
        nameserverLabel.setText("nameserver:");
        loginButton.setLabel("Enter");

        //init components - panel 2
        removeButton = new java.awt.Button();
        openButton = new java.awt.Button();
        removeLocalButton = new java.awt.Button();
        logoutButton = new java.awt.Button();
        logText = new java.awt.TextArea();
        jScrollPane = new JScrollPane(controller.getList());
        removeButton.setLabel("Remove File");
        openButton.setLabel("Open File");
        removeLocalButton.setLabel("Remove Local File");
        logoutButton.setLabel("EXIT");

        //GUI border
        panel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Project: System Y - Node name: " + Main.nodeName));
        setName("SystemY"); // NOI18N



        //panel 1 layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(nodenameLabel)
                                                        .addComponent(nameserverLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(nameserverTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(nodenameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nodenameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nodenameLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameserverTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nameserverLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(loginButton)
                                .addGap(5, 5, 5))
        );





        //panel 2 layout
        layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(layout);
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
        if (firstLogin){
            setSize(250, 140);
            add(panel1);
        }
        else{
            setSize(500, 330);
            add(panel2);
        }
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
    public void loginButtonListener(ActionListener event) {
        setIpNameserver(nameserverTextField.getText());
        setNodename(nodenameTextField.getText());
        this.loginButton.addActionListener(event);
    }

    public void writeLogs(String logText){this.logText.append(printStamp() + logText + "\n");}
    public String printStamp(){
        LocalDateTime now = LocalDateTime.now();
        return "[" + ((now.getHour() < 10 ? "0" : "") + now.getHour()) + ":"
                + ((now.getMinute() < 10 ? "0" : "") + now.getMinute()) + ":"
                + ((now.getSecond() < 10 ? "0" : "") + now.getSecond()) + "]\t";
    }
}
