package com.sunzequn.sdfs.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sloriac on 16-12-19.
 */
public class UserClient extends JFrame {


    private Container container;
    private BoxLayout boxLayout;
    private JPanel upperPanel;
    private JPanel midPanel;
    private JPanel bottomPanel;
    private JTextField portTextField;
    private JButton connectButton;
    private JButton exitButton;
    private JLabel infoLabel;

    public UserClient(String name) throws HeadlessException {
        super(name);
        init();
        initUpperPanel();
        initMidPanel();
        initBottomPanel();
    }


    private void init() {
        container = getContentPane();
        boxLayout = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(boxLayout);
    }

    private void initUpperPanel() {
        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        upperPanel.setPreferredSize(new Dimension(400, 180));
        this.add(upperPanel);
    }

    private void initMidPanel() {
        midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.setPreferredSize(new Dimension(400, 30));
        JLabel portLabel = new JLabel(" IP:端口号 ");
        midPanel.add(portLabel);

        portTextField = new JTextField();
        midPanel.add(portTextField);

        connectButton = new JButton("连接");
        infoLabel = new JLabel("", JLabel.CENTER);

        exitButton = new JButton("离开");
        UserClientListener listener = new UserClientListener(portTextField, connectButton, exitButton, infoLabel);
        connectButton.addActionListener(listener);
        exitButton.addActionListener(listener);

        midPanel.add(connectButton);
        midPanel.add(exitButton);
        this.add(midPanel);
    }


    private void initBottomPanel() {
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(400, 30));
        bottomPanel.add(infoLabel);

        this.add(bottomPanel);
    }
}