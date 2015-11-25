/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.adapter.v1;

//目标接口Target  
interface Targetable{  
 // 与源类相同的接口函数  
 public void operation1();  
 // 新的接口函数,源类中没有  
 public void operation2();  
} 
