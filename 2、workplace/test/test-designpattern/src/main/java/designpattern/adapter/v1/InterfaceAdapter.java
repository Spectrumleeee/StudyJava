/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.adapter.v1;

//源接口  
interface Sourceable{  
   
  public void operation1();  
  public void operation2();  
}  

//默认适配器类DefaultWrapper  
abstract class DefaultWrapper implements Sourceable{  

  public void operation1(){
      
  }  
  public void operation2(){
      
  }  
}  

//两个实现类,可以选择自己需要的实现  
class SourceSub1 extends DefaultWrapper{  

  public void operation1(){  

    System.out.println("源接口的一个实现子类Sub1");  
    
  }  
}  

class SourceSub2 extends DefaultWrapper{  

  public void operation2(){  

    System.out.println("源接口的一个实现子类Sub2");  
    
  }  
} 
