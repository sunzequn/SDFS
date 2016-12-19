package com.sunzequn.sdfs.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sloriac on 16-12-19.
 */
public class UserClient extends JFrame {


    private Container container;
    private BoxLayout boxLayout;
    private JScrollPane upperPanel;
    private JPanel midPanel;
    private JPanel bottomPanel;
    private JTextField portTextField;
    private JButton connectButton;
    private JButton exitButton;
    private JButton uploadButton;
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
        upperPanel = new JScrollPane();
        upperPanel.setBackground(Color.black);
        upperPanel.setPreferredSize(new Dimension(400, 180));

        //scrollPane.add();
//        upperPanel.setViewportView();
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

        UserClientListener listener = new UserClientListener(upperPanel, portTextField, connectButton, infoLabel);
        connectButton.addActionListener(listener);


        uploadButton = new JButton("上传");
        uploadButton.addActionListener(new UploadListener(midPanel));

        midPanel.add(connectButton);
        midPanel.add(uploadButton);
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
