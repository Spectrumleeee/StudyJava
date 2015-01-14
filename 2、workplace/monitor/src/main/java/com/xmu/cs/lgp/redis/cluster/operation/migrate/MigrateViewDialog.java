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
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.xmu.cs.lgp.redis.cluster.monitor.MonitorClient;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import org.json.JSONObject;

/**
 * MigrateViewDialog.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 13, 2015
 */
public class MigrateViewDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField tf_source;
    private JTextField tf_target;
    private JTextField tf_slotNum;
    private JButton btnMigrate;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            MigrateViewDialog dialog = new MigrateViewDialog(null, null, null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog. Edited with WindowBuilder and MyEclipse2014
     */
    public MigrateViewDialog(final MigrateStructure migrateStructure,
            final MonitorClient monitorClient, JButton btnMigrate) {
        this.btnMigrate = btnMigrate;
        setBounds(100, 100, 235, 303);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setToolTipText("");
        contentPanel.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Migrate",
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        FlowLayout fl_contentPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
        contentPanel.setLayout(fl_contentPanel);
        {
            JPanel panel = new JPanel();
            panel.setBorder(new TitledBorder(null, "Source",
                    TitledBorder.LEADING, TitledBorder.TOP, null, null));
            contentPanel.add(panel);
            {
                tf_source = new JTextField();
                tf_source.setEditable(false);
                panel.add(tf_source);
                tf_source.setColumns(15);
            }
        }
        {
            JPanel panel = new JPanel();
            panel.setBorder(new TitledBorder(null, "Target",
                    TitledBorder.LEADING, TitledBorder.TOP, null, null));
            contentPanel.add(panel);
            {
                tf_target = new JTextField();
                tf_target.setEditable(false);
                panel.add(tf_target);
                tf_target.setColumns(15);
            }
        }
        {
            JPanel panel = new JPanel();
            panel.setBorder(new TitledBorder(null, "Slot Number",
                    TitledBorder.LEADING, TitledBorder.TOP, null, null));
            contentPanel.add(panel);
            {
                tf_slotNum = new JTextField();
                panel.add(tf_slotNum);
                tf_slotNum.setColumns(15);
            }
        }
        {
            if (migrateStructure != null) {
                tf_source.setText(migrateStructure.getSource());
                tf_target.setText(migrateStructure.getTarget());
                tf_slotNum.setText("< "
                        + migrateStructure.getCurrentSlotsNums());
            }
            tf_slotNum.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    int keyChar = e.getKeyChar();
                    if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9
                            && tf_slotNum.getText().length() < 5) {
                    } else {
                        e.consume();
                    }
                }
            });
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                final JLabel label_status = new JLabel("");
                buttonPane.add(label_status);

                final JButton okButton = new JButton("START");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (monitorClient == null)
                            return;
                        String slotsNum = tf_slotNum.getText();
                        if (migrateStructure == null
                                || !migrateStructure
                                        .checkInputSlotsNums(slotsNum))
                            return;
                        if (!migrateStructure.isFlag()) {
                            System.err
                                    .println("[INFO] there is a running slot-migrate thread!!");
                            return;
                        } else
                            migrateStructure.setFlag(false);
                        okButton.setEnabled(false);

                        String command = "migrate#"
                                + migrateStructure.getSource() + "#"
                                + migrateStructure.getTarget() + "#"
                                + migrateStructure.getMigrateSlotsNums();
                        String rst;
                        try{
                            rst = monitorClient.sendMessage(command);
                        }catch(Exception ee){
                            label_status.setText("Server is Down");
                            return;
                        }
                        label_status.setText(new JSONObject(rst).getString("Msg"));
                        okButton.setEnabled(true);
                        migrateStructure.setFlag(true);
                    }
                });
                {
                    JLabel lblNewLabel = new JLabel("     ");
                    buttonPane.add(lblNewLabel);
                }
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
    }

    /*
     * @see javax.swing.JDialog#processWindowEvent(java.awt.event.WindowEvent)
     */
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (JOptionPane.showConfirmDialog(this, "确实要关闭？", "确认",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (btnMigrate != null)
                    btnMigrate.setEnabled(true);
                this.dispose();
            } else {
            }
        } else {
            super.processWindowEvent(e);
        }
    }
}
