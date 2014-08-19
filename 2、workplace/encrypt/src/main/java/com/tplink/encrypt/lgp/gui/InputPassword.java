/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 18, 2014
 */

package com.tplink.encrypt.lgp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tplink.encrypt.lgp.encrypt.CryptTool;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JPasswordField;

public class InputPassword extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private static final int ENCRYPT = 1;
    private static final int DECRYPT = 0;
    private JPasswordField passwordField;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            InputPassword dialog = new InputPassword(null, 0, null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public InputPassword(final String fileIn, final int mode, final String dir) {
        setBounds(100, 100, 294, 115);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        {
            passwordField = new JPasswordField();
            passwordField.setColumns(12);
            contentPanel.add(passwordField);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    @SuppressWarnings("deprecation")
                    public void actionPerformed(ActionEvent e) {
                        CryptTool tool = new CryptTool("AES");
                        try {
                            if(ENCRYPT == mode){
                                tool.EncryptFile(fileIn, passwordField.getText());
                                File sourceFile = new File(fileIn + "_enc");
                                File targetFile = new File(dir + ".enc");
                                sourceFile.renameTo(targetFile);     
                            }
                            else if(DECRYPT == mode){
                                tool.DecryptFile(fileIn, passwordField.getText());
                                File sourceFile = new File(fileIn + "_dec");
                                File targetFile = new File(dir + ".dec");
                                sourceFile.renameTo(targetFile); 
                            }
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }finally{
                            dispose();
                        }
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

}
