package com.xmu.lgp.TestXML;

import java.io.InputStream;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.xmu.lgp.TestXML.TestSAX_Book;
import com.xmu.lgp.TestXML.TestSAX_University;
import com.xmu.lgp.entity.Book;
import com.xmu.lgp.entity.Classes;
import com.xmu.lgp.entity.College;
import com.xmu.lgp.entity.Student;

/**
 * T_SAX.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-9
 */
public class T_SAX {
    
    @Ignore
    public void testSAX() throws Exception{
        TestSAX_Book sax = new TestSAX_Book();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("books.xml");
        List<Book> books = sax.getBooks(input);
        for(Book book : books)
            System.out.println(book.toString());
    }
    
    @Test
    public void testSAX_university() throws Exception{
        TestSAX_University sax = new TestSAX_University();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("university.xml");
        List<College> university = sax.getUniversity(input);
        for(College col : university){
            System.out.println(col.getName());
            for(Classes cls : col.getClasses()){
                System.out.println("\t"+cls.getName());
                for(Student stu : cls.getStudents()){
                    System.out.println("\t\t"+stu.toString());
                }
            }
        }
    }
}
