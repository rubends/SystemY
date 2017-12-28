package be.ua;

import javax.swing.*;

import static be.ua.Main.controller;

public class GuiPanel extends javax.swing.JPanel {

    public GuiPanel() {
        initComponents();
    }

    private void initComponents() {
        removeButton = new java.awt.Button();
        openButton = new java.awt.Button();
        removeLocalButton = new java.awt.Button();
        logoutButton = new java.awt.Button();
        downloadButton = new java.awt.Button();
        logText = new java.awt.TextArea();
        /*jScrollPane = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList<>();
        jScrollPane.setViewportView(controller.getList());*/
        jScrollPane = new JScrollPane(controller.getList());

        setBorder(javax.swing.BorderFactory.createTitledBorder("System Y"));
        setName("SystemY"); // NOI18N

        removeButton.setActionCommand("Remove File");
        removeButton.setLabel("Remove File");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        openButton.setLabel("Open File");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        removeLocalButton.setLabel("Remove Local File");
        removeLocalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLocalButtonActionPerformed(evt);
            }
        });

        logoutButton.setLabel("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        downloadButton.setLabel("Download File");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
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
                                                        .addComponent(downloadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                                .addComponent(downloadButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controller.removeButtonClicked(fileList.getSelectedValue());
    }

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controller.openButtonClicked(fileList.getSelectedValue());
    }

    private void removeLocalButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controller.removeLocalButtonClicked(fileList.getSelectedValue());
    }

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //controller.downloadButtonClicked();
        //System.out.println(jScrollPane);
        //System.out.println(fileList.getSelectedValue());
        //System.out.println(fileList.getModel());
    }

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controller.logoutButtonClicked();
    }

    private java.awt.Button downloadButton;
    private javax.swing.JList<String> fileList;
    private javax.swing.JScrollPane jScrollPane;
    private java.awt.TextArea logText;
    private java.awt.Button logoutButton;
    private java.awt.Button openButton;
    private java.awt.Button removeButton;
    private java.awt.Button removeLocalButton;
}
