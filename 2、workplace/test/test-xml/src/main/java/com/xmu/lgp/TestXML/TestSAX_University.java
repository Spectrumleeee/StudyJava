package com.xmu.lgp.TestXML;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xmu.lgp.entity.Classes;
import com.xmu.lgp.entity.College;
import com.xmu.lgp.entity.Student;

/**
 * 演示SAX用法， 这里讲XML文件解析保存到内存中，实际上SAX的优势是处理大文件，类似流媒体方式，读取即处理
 * 如果将真个XML文件读取并保存进内存，就不是SAX的优势所在，这里只是为了演示SAX的使用方法。
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: 2015-3-9
 */
public class TestSAX_University extends DefaultHandler {
    private List<College> universities;
    private List<Classes> classes;
    private List<Student> students;
    private College col;
    private Classes cls;
    private Student stu;

    public List<College> getUniversity(InputStream xmlStream) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(xmlStream, this);
        return this.getUniversity();
    }

    public List<College> getUniversity() {
        return universities;
    }
    
    //主要就是重写以下4个方法，一行一行地解析XML文件
    @Override
    public void startDocument() throws SAXException {
        universities = new ArrayList<College>();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if ("college".equals(qName)) {
            col = new College();
            col.setName(attributes.getValue(0));
            classes = new ArrayList<Classes>();
        } else if ("class".equals(qName)) {
            cls = new Classes();
            cls.setName(attributes.getValue(0));
            students = new ArrayList<Student>();
        } else if ("student".equals(qName)) {
            stu = new Student();
            stu.setName(attributes.getValue(0));
            stu.setSex(attributes.getValue(1));
            stu.setAge(Integer.parseInt(attributes.getValue(2)));
        }
        // System.out.println("Start-"+qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("college".equals(qName)) {
            col.setClasses(classes);
            universities.add(col);
            col = null;
            classes = null;
        } else if ("class".equals(qName)) {
            cls.setStudents(students);
            classes.add(cls);
            cls = null;
            students = null;
        } else if ("student".equals(qName)) {
            students.add(stu);
            stu = null;
        }
        // System.out.println("End-"+qName);
    }

    // 处理两个标签中的值，而不是属性的值，比如<name>spectrumleeee</name>
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }
}
