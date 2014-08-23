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
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JSplitPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JTextArea;
import java.awt.Color;

public class Window_AES extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_CRYPT_DIR = "D:\\Documents and Settings\\Documents\\GitHub\\TP-LINK\\document";
    private static final String DEFAULT_ENCRYPT_DIR = "D:\\TEST";
    private JPanel contentPane;
    private JTextField selected_file_to_encrypt;
    private JTextField selected_file_to_decrypt;
    private File selectedFile;
    private int encOrdec = 1;  // default 1,encrypt, 0 stands for decrypt
    private JButton btnStartEncdec;
    private JSplitPane splitPane_3;
    private JSplitPane splitPane_4;
    private JButton button;
    private JTextArea infoArea;
    private static String dir = "D:\\Documents and Settings\\Desktop";
    private JButton button_1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Window_AES frame = new Window_AES();
                    frame.setTitle("File Encrypt|Decrypt Tool Based On Java AES");
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
    public Window_AES() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 400, 800, 254);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        JSplitPane splitPane = new JSplitPane();
        contentPane.add(splitPane, BorderLayout.NORTH);
        
        JButton btnAddFile = new JButton("选择加密文件:");
        btnAddFile.setSize(30, 20);
        btnAddFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Hello World!");
                JFileChooser chooser = new JFileChooser(new File(DEFAULT_ENCRYPT_DIR));
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle("Select a file to encrypt!");
                
                int result = chooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    selectedFile = chooser.getSelectedFile();
                    selected_file_to_decrypt.setText("");
                    selected_file_to_encrypt.setText(selectedFile.getPath());
                    encOrdec = 1;
                    btnStartEncdec.setEnabled(true);
                }
                
            }
        });
        splitPane.setLeftComponent(btnAddFile);
        
        selected_file_to_encrypt = new JTextField();
        splitPane.setRightComponent(selected_file_to_encrypt);
        selected_file_to_encrypt.setColumns(10);
        
        JSplitPane splitPane_1 = new JSplitPane();
        contentPane.add(splitPane_1, BorderLayout.SOUTH);
        
        JButton btnDeleteFile = new JButton("选择解密文件 :");
        btnDeleteFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                JFileChooser chooser = new JFileChooser(new File(DEFAULT_CRYPT_DIR));
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle("Select a file to decrypt!");
                
                int result = chooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    selectedFile = chooser.getSelectedFile();
                    selected_file_to_encrypt.setText("");
                    selected_file_to_decrypt.setText(selectedFile.getPath());
                    encOrdec = 0;
                    btnStartEncdec.setEnabled(true);
                }
            }
        });
        splitPane_1.setLeftComponent(btnDeleteFile);
        
        selected_file_to_decrypt = new JTextField();
        splitPane_1.setRightComponent(selected_file_to_decrypt);
        selected_file_to_decrypt.setColumns(10);
        
        JSplitPane splitPane_2 = new JSplitPane();
        contentPane.add(splitPane_2, BorderLayout.CENTER);
        
        btnStartEncdec = new JButton("开 始 加 解 密 :");
        btnStartEncdec.setEnabled(false);
        btnStartEncdec.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
            public void actionPerformed(ActionEvent e) {
                String tempDir = dir + "\\" + selectedFile.getName();
                InputPassword inputPass = new InputPassword(selectedFile.getPath(), encOrdec, tempDir);
                inputPass.setTitle("请输入密码");
                inputPass.show();
                btnStartEncdec.setEnabled(false);
            }
        });
        splitPane_2.setLeftComponent(btnStartEncdec);
        
        splitPane_3 = new JSplitPane();
        splitPane_2.setRightComponent(splitPane_3);
        
        splitPane_4 = new JSplitPane();
        splitPane_4.setBackground(Color.WHITE);
        splitPane_4.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_3.setLeftComponent(splitPane_4);
        
        button = new JButton("加解密后文件位置");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(new File(DEFAULT_CRYPT_DIR));
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogTitle("Select a file to encrypt!");
                
                int result = chooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    infoArea.append("设置加解密后存储路径为:\n" + chooser.getSelectedFile().getPath()+"\n");
                    dir = chooser.getSelectedFile().getPath();
                    System.out.println(dir);
                }
            }
        });
        splitPane_4.setLeftComponent(button);
        
        button_1 = new JButton("");
        button_1.setBackground(Color.YELLOW);
        splitPane_4.setRightComponent(button_1);
        
        infoArea = new JTextArea();
        infoArea.append("默认加解密后存储路径为:\n" + dir + "\n");
        splitPane_3.setRightComponent(infoArea);
    }
}
