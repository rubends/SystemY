package be.ua;

import javax.swing.*;

public class View extends JFrame{
    FileAgent fileAgent;
    JPanel panel;

    public View(FileAgent fileAgent)
    {
        this.fileAgent = fileAgent; //todo FileAgent
        setSize(500, 400);
        panel = new GuiPanel();
        add(panel);
    }
}
