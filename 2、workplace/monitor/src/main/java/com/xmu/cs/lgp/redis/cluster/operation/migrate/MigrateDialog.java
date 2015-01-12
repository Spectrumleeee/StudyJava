/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.xmu.cs.lgp.redis.cluster.operation.migrate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import com.xmu.cs.lgp.redis.cluster.tools.JedisTools;

/**
 * SlotsDialog.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Dec 29, 2014
 */
public class MigrateDialog extends JDialog {

    private static final long serialVersionUID = 4946121383948183228L;
    private final JPanel contentPanel = new JPanel();
    private JTextField textField;
    private JButton jb;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            MigrateDialog dialog = new MigrateDialog(null, null, null, null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setTitle("Set the number of slots");
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public MigrateDialog(final JedisTools jd, final Object lock, final JButton jb,
            final MigrateStructure migrateStructure) {
        this.jb = jb;
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = scrSize.width;
        int screenHeight = scrSize.height;
        setBounds((screenWidth - 450) / 2, (screenHeight - 300) / 2, 500, 200);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(new GridLayout(3, 1));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(100, 30));
        JPanel emptyPane = new JPanel();
        emptyPane.setBounds(0, 0, 100, 30);
        contentPanel.add(emptyPane);
        JPanel centerPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JLabel lbSource = new JLabel("Source");
        centerPane.add(lbSource);
        centerPane.add(textField);
        contentPanel.add(centerPane);
        textField.setColumns(8);
        
        JLabel lblTarget = new JLabel("Target");
        if(migrateStructure != null){
            lbSource.setText(migrateStructure.getSource() + " migrate ");
            lblTarget.setText(" slots to " + migrateStructure.getTarget());
        }
        centerPane.add(lblTarget);

        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9
                        && textField.getText().length() < 5) {
                } else {
                    e.consume();
                }
            }
        });

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        final JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String slotsNum = textField.getText();
                if (migrateStructure==null || !migrateStructure.checkInputSlotsNums(slotsNum))
                    return;
                if (!migrateStructure.isFlag()) {
                    System.err
                            .println("[INFO] there is a running slot-migrate thread!!");
                    return;
                } else
                    migrateStructure.setFlag(false);
                okButton.setEnabled(false);
                Thread migrateThread = new MigrateThread(jd, lock, okButton,
                        migrateStructure);
                migrateThread.start();
            }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
            }
        });
        buttonPane.add(cancelButton);

    }

    /*
     * @see javax.swing.JDialog#processWindowEvent(java.awt.event.WindowEvent)
     */
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (JOptionPane.showConfirmDialog(this, "确实要关闭？", "确认",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (jb != null)
                    jb.setEnabled(true);
                this.dispose();
            } else {
            }
        } else {
            super.processWindowEvent(e);
        }
    }
}
