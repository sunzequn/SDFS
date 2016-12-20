package com.sunzequn.sdfs.test;

import com.sunzequn.sdfs.ui.UserClient;
import org.junit.Test;

import javax.swing.*;

/**
 * Created by sloriac on 16-12-19.
 */
public class UiTest {

    public static void main(String[] args) {
        UserClient myFrame = new UserClient("客户端");
        myFrame.setSize(400, 260);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
    }

}
