/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.adapter.v1;

//包装器模式  
public class ObjectAdapter implements Targetable{  
  //源类对象  
  private Source source;  
  //取得源类对象  
  public ObjectAdapter(Source source){  

    super();  
    this.source = source;  
  }  
    
  //调用源类对象的方法  
  public void operation1(){  
    
    source.operation1();  
  }  
 
  //实现目标类的新接口函数  
  public void operation2(){  

    System.out.println("包装目标类后的方法");  
  }  
} 