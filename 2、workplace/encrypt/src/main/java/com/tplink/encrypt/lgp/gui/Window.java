/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 15, 2014
 */

package com.tplink.encrypt.lgp.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Window extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Window frame = new Window();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        JSplitPane splitPane = new JSplitPane();
        contentPane.add(splitPane, BorderLayout.NORTH);
        
        JButton btnAddFile = new JButton(" Add File :");
        btnAddFile.setSize(30, 20);
        btnAddFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello World!");
            }
        });
        splitPane.setLeftComponent(btnAddFile);
        
        textField = new JTextField();
        splitPane.setRightComponent(textField);
        textField.setColumns(10);
        
        JSplitPane splitPane_1 = new JSplitPane();
        contentPane.add(splitPane_1, BorderLayout.SOUTH);
        
        JButton btnDeleteFile = new JButton("Dele File :");
        btnDeleteFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        splitPane_1.setLeftComponent(btnDeleteFile);
        
        textField_1 = new JTextField();
        splitPane_1.setRightComponent(textField_1);
        textField_1.setColumns(10);
        
        JSplitPane splitPane_2 = new JSplitPane();
        contentPane.add(splitPane_2, BorderLayout.CENTER);
        
        JButton btnDirectory = new JButton("Directory");
        splitPane_2.setLeftComponent(btnDirectory);
        
        textField_2 = new JTextField();
        splitPane_2.setRightComponent(textField_2);
        textField_2.setColumns(10);
    }

}
