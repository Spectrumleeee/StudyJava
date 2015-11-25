/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 19, 2014
 */

package com.xmu.lgp.TestXML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/*
 * 基于 DOM 操作XML文件
 */
public class TestDom_University {
    // 创建DocumentBuilderFactory解析工厂
    static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public static void main(String[] args) {
        read();
        write();
    }

    /*
     * 使用 DOM 解析XML文件
     */
    public static void read() {
        try {
            // 由DocumentBuilderFactory实例一个DocumentBuilder
            DocumentBuilder builder = dbf.newDocumentBuilder();
            // 读取要解析的XML文件到输入流
            InputStream in = TestDom_University.class.getClassLoader()
                    .getResourceAsStream("university.xml");
            // 用DocumentBuilder通过输入流构建Document实例（对应XML文件）
            Document doc = builder.parse(in);
            // 获取Document实例的第一个Element,即root元素
            Element root = doc.getDocumentElement();
            if (root == null)
                return;
            System.err.println(root.getAttribute("name"));
            // 获取Element元素的所有college子节点Node
            NodeList collegeNodes = root.getChildNodes();
            if (collegeNodes == null)
                return;
            for (int i = 0; i < collegeNodes.getLength(); i++) {
                Node college = collegeNodes.item(i);
                if (college != null
                        && college.getNodeType() == Node.ELEMENT_NODE) {
                    System.err.println("\t"
                            + college.getAttributes().getNamedItem("name")
                                    .getNodeValue());
                    // 获取college节点的class子节点
                    NodeList classNodes = college.getChildNodes();
                    if (classNodes == null)
                        continue;
                    for (int j = 0; j < classNodes.getLength(); j++) {
                        Node clazz = classNodes.item(j);
                        if (clazz != null
                                && clazz.getNodeType() == Node.ELEMENT_NODE) {
                            System.err.println("\t\t"
                                    + clazz.getAttributes()
                                            .getNamedItem("name")
                                            .getNodeValue());
                            // 获取class节点的student子节点
                            NodeList studentNodes = clazz.getChildNodes();
                            if (studentNodes == null)
                                continue;
                            for (int k = 0; k < studentNodes.getLength(); k++) {
                                Node student = studentNodes.item(k);
                                if (student != null
                                        && student.getNodeType() == Node.ELEMENT_NODE) {
                                    System.err.print("\t\t\t"
                                            + student.getAttributes()
                                                    .getNamedItem("name")
                                                    .getNodeValue());
                                    System.err.print(" "
                                            + student.getAttributes()
                                                    .getNamedItem("sex")
                                                    .getNodeValue());
                                    System.err.println(" "
                                            + student.getAttributes()
                                                    .getNamedItem("age")
                                                    .getNodeValue());
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 使用 DOM 修改重写一个 XML文件
     */
    public static void write() {
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = TestDom_University.class.getClassLoader()
                    .getResourceAsStream("university.xml");
            Document doc = builder.parse(in);
            Element root = doc.getDocumentElement();
            if (root == null)
                return;
            root.setAttribute("name", "nku");
            NodeList collegeNodes = root.getChildNodes();
            if (collegeNodes != null) {
                for (int i = 0; i < collegeNodes.getLength() - 1; i++) {
                    // delete a note
                    Node college = collegeNodes.item(i);
                    if (college.getNodeType() == Node.ELEMENT_NODE) {
                        String collegeName = college.getAttributes()
                                .getNamedItem("name").getNodeValue();
                        if ("c1".equals(collegeName)
                                || "c2".equals(collegeName)) {
                            root.removeChild(college);
                        } else if ("c3".equals(collegeName)) {
                            Element newChild = doc.createElement("class");
                            newChild.setAttribute("name", "class4");
                            college.appendChild(newChild);
                        }
                    }
                }
            }
            // 创建一个新节点，要使用Element
            Element addCollege = doc.createElement("college");
            addCollege.setAttribute("name", "finance");
            root.appendChild(addCollege);
            Text text = doc.createTextNode("text");
            addCollege.appendChild(text);

            // 由转化工厂生成转化者
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transFormer = transFactory.newTransformer();
            // 将Document作为DOMSource作为转化源
            DOMSource domSource = new DOMSource(doc);
            // 创建一个文件，保存结果
            File file = new File("university-dom.xml");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            // 转化目的地址为StreamResult
            StreamResult xmlResult = new StreamResult(out);
            // 最终转化者将DOMSource转化为StreamResult
            transFormer.transform(domSource, xmlResult);
            System.out.println(file.getAbsolutePath());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
