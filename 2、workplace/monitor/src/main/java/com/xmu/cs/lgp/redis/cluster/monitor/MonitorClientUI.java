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
package com.xmu.cs.lgp.redis.cluster.monitor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.RowSorter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import com.xmu.cs.lgp.redis.cluster.operation.migrate.MigrateStructure;
import com.xmu.cs.lgp.redis.cluster.operation.migrate.MigrateViewDialog;
import com.xmu.cs.lgp.redis.cluster.process.parser.JsonParser;
import com.xmu.cs.lgp.redis.cluster.process.parser.MemoryJsonParser;
import com.xmu.cs.lgp.redis.cluster.process.parser.SlotsJsonParser;
import com.xmu.cs.lgp.redis.cluster.tools.JedisTools;
import com.xmu.cs.lgp.redis.cluster.tools.RefreshThread;

/**
 * Monitor.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Dec 15, 2014
 */
public class MonitorClientUI extends JFrame {

    /*
     * This UI program can show the redis cluster memory/slots info, It connects
     * to the MonitorServer by using MonitorClient api , and the MonitorServer 
     * is a RedisClusterProxy, it execute the client command by using Jedis api.
     */

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel northPane;
    private JPanel southPane;
    private JPanel centerPane;
    private JScrollPane tablePane;
    private JTable memoryTable;
    private JTable slotsTable;
    private CardLayout layout;
    private JedisTools jtl;
    private JButton btnMigrate;
    private Object lock = new Object();
    private JLabel lblSourceNode;
    private JComboBox<String> cbxSource;
    private JLabel lblTargetNode;
    private JComboBox<String> cbxTarget;
    private MigrateViewDialog migrateDialog;
    private Object[][] data;
    private MonitorClient client;
    private String[] columnNames = { "Node", "Node-status", "Mem-used",
            "Mem-used-peak", "MaxMemory", "Percent(used/max)" };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MonitorClientUI frame = new MonitorClientUI();
                    frame.setTitle("Redis Cluster Monitor");
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
    public MonitorClientUI() {
        client = new MonitorClient();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = scrSize.width;
        int screenHeight = scrSize.height;
        setBounds((screenWidth - 800) / 2, (screenHeight - 500) / 3, 800, 500);
        // setBounds((screenWidth + 200), (screenHeight - 500) / 3, 800, 500);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("\u6587\u4EF6");
        menuBar.add(menu);

        JMenu menu_1 = new JMenu("\u9009\u9879");
        menuBar.add(menu_1);

        JMenu menu_2 = new JMenu("\u5173\u4E8E");
        menuBar.add(menu_2);

        JMenuItem mntmRedisVersion = new JMenuItem("Redis Version");
        mntmRedisVersion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        menu_2.add(mntmRedisVersion);

        final JButton showMemoryBtn = new JButton("Memory");
        showMemoryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMemoryTable();
                layout.show(northPane, "memory");
            }
        });

        final JButton showSlotsBtn = new JButton("Slots");
        showSlotsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSlotTable(true);
                layout.show(northPane, "slots");
                southPane.setVisible(true);
            }
        });

        btnMigrate = new JButton("Migrate");
        final MigrateStructure migrateStructure = new MigrateStructure();
        btnMigrate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!checkSourceTarget())
                    return;
                migrateStructure.setCurrentSlotsNums((int) data[cbxSource
                        .getSelectedIndex()][1]);
                migrateStructure.setSource((String) cbxSource.getSelectedItem());
                migrateStructure.setTarget((String) cbxTarget.getSelectedItem());

                // migrateDialog = new MigrateDialog(jtl, lock, btnMigrate,
                // migrateStructure);
                migrateDialog = new MigrateViewDialog(migrateStructure, client,
                        btnMigrate);
                migrateDialog.setTitle("Slots Migrating");
                migrateDialog.setResizable(false);
                migrateDialog.setVisible(true);
                btnMigrate.setEnabled(false);
                // updateSlotTable(false);
            }
        });
        // set the size of button
        Dimension preferredSize = new Dimension(100, 50);
        showMemoryBtn.setPreferredSize(preferredSize);
        showSlotsBtn.setPreferredSize(preferredSize);
        btnMigrate.setPreferredSize(preferredSize);
        // use CardLayout to switch memory or slots panel
        layout = new CardLayout();
        northPane = new JPanel(layout);

        initMemoryTable();
        tablePane = new JScrollPane(memoryTable);
        tablePane.setPreferredSize(new Dimension(800, 350));
        northPane.add(tablePane, "memory");

        initSlotsTable();
        tablePane = new JScrollPane(slotsTable);
        tablePane.setPreferredSize(new Dimension(800, 350));
        northPane.add(tablePane, "slots");

        southPane = new JPanel();
        southPane.setLayout(new FlowLayout(FlowLayout.CENTER));

        centerPane = new JPanel();
        centerPane.setLayout(new FlowLayout());
        centerPane.add(showMemoryBtn);
        centerPane.add(showSlotsBtn);

        lblSourceNode = new JLabel("Source Node:");
        southPane.add(lblSourceNode);

        cbxSource = new JComboBox<String>();
        southPane.add(cbxSource);

        lblTargetNode = new JLabel("Target Node:");
        southPane.add(lblTargetNode);

        cbxTarget = new JComboBox<String>();
        southPane.add(cbxTarget);
        southPane.add(btnMigrate);

        centerPane.add(southPane);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));

        contentPane.add(northPane, BorderLayout.NORTH);
        // contentPane.add(southPane, BorderLayout.SOUTH);
        contentPane.add(centerPane, BorderLayout.CENTER);

        setContentPane(contentPane);

        southPane.setVisible(false);
    }

    /*
     * Initiate the memory table
     */
    public void initMemoryTable() {
        jtl = new JedisTools(lock);
        new RefreshThread(jtl, lock).start();

        Object[][] data = {
                { "Kathy", "OK", "Smith", "Snowboarding", new Integer(5),
                        new Float(0.0f) },
                { "John", "OK", "Doe", "Rowing", new Integer(3),
                        new Float(0.0f) },
                { "Sue", "OK", "Black", "Knitting", new Integer(2),
                        new Float(0.0f) },
                { "Jane", "OK", "White", "Speed reading", new Integer(20),
                        new Float(0.0f) },
                { "Joe", "OK", "Brown", "Pool", new Integer(10),
                        new Float(0.0f) } };
        TableModel model = new DefaultTableModel(data, columnNames);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        memoryTable = new JTable(model);
        memoryTable.setRowSorter(sorter);
        memoryTable.setSelectionBackground(new Color(65535));
        memoryTable.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component render = new DefaultTableCellRenderer()
                        .getTableCellRendererComponent(table, value,
                                isSelected, hasFocus, row, column);
                float percent = 0.0f;
                try {
                    percent = (float) table.getValueAt(row, 5);
                } catch (Exception e) {
                    percent = 0.0f;
                }

                if ("FAIL".equals(table.getValueAt(row, 1))) {
                    render.setBackground(Color.RED);
                }
                if (percent > 20.0f)
                    render.setBackground(Color.GREEN);

                return render;
            }
        });
    }

    /*
     * Initiate the slot table
     */
    public void initSlotsTable() {
        String[] columnNames = { "Node", "Slot" };
        Object[][] data = { { "Node-A", new Integer(5461) },
                { "Node-B", new Integer(5461) },
                { "Node-C", new Integer(5462) } };
        TableModel model = new DefaultTableModel(data, columnNames);
        slotsTable = new JTable(model);
        slotsTable.setAutoCreateRowSorter(true);
    }

    /*
     * Update the slot info table
     */
    public void updateSlotTable(boolean initSourceTarget) {
        String[] columnNames = { "Node", "Slot" };
        String rst;
        try {
            rst = client.sendMessage("getSlotsInfo");
        } catch (Exception e) {
            return;
        }
        JsonParser jp = new SlotsJsonParser();
        data = jp.parse(rst);
        // data = jtl.getSlotsInfo();
        TableModel model = new DefaultTableModel(data, columnNames);
        slotsTable.setModel(model);
        if (initSourceTarget)
            initSoruceTargetCombox(data);
    }

    /*
     * Update the memory info table
     */
    public void updateMemoryTable() {

        String rst;
        try {
            rst = client.sendMessage("getMemoryInfo");
        } catch (Exception e) {
            return;
        }
        JsonParser jp = new MemoryJsonParser();
        Object[][] data = jp.parse(rst);
        // data = jtl.getMemoryInfo();
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
                tableModel);
        // table.setAutoCreateRowSorter(true);
        memoryTable.setRowSorter(sorter);
        memoryTable.setModel(tableModel);
    }

    /*
     * initiate the Combox that contain the source and target master node
     */
    public void initSoruceTargetCombox(Object[][] obj) {
        int nodeNums = obj.length;
        cbxSource.removeAllItems();
        cbxTarget.removeAllItems();
        for (int i = 0; i < nodeNums; i++) {
            cbxSource.addItem((String) obj[i][0]);
            cbxTarget.addItem((String) obj[i][0]);
        }
    }

    /*
     * check the source and target node to guarentee that they are not the same
     */
    public boolean checkSourceTarget() {
        if (cbxSource.getSelectedIndex() == cbxTarget.getSelectedIndex()) {
            System.err.println("[INFO] can't migrate slots to the same node!!");
            return false;
        }
        return true;
    }
}
