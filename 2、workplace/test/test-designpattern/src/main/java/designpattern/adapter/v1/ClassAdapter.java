/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.adapter.v1;

 // 类适配器模式,继承源类,并实现目标接口  
  public class ClassAdapter extends Source implements Targetable{  
    
    // 实现目标类的新接口函数  
    public void operation2(){  
    
      System.out.println("适配目标类后的方法");  
      
    }  
  } 
