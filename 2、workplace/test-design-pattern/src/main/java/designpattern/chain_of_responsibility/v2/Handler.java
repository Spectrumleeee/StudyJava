/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

/**
 * every chain can be singly-linked or double-linked
 * below can implements a singly-linked filter , if 
 * double-linked, must cantains two ponters: preview 
 * pointer & successor pointer.
 * 
 */

/**
 * 职责链可以是一条直线、一个环或者一个树形结构，最常见的职责链是直线型，即沿着一条单向的链来传递请求。
 * 链上的每一个对象都是请求处理者，职责链模式可以将请求的处理者组织成一条链，并使请求沿着链传递，由链上的处理者对请求进行相应的处理，
 * 客户端无须关心请求的处理细节以及请求的传递，只需将请求发送到链上即可，将请求的发送者和请求的处理者解耦。这就是职责链模式的模式动机。
 */

/**
 * 行为型模式是对在不同的对象之间划分责任和算法的抽象化。行为型模式不仅仅关注类和对象的结构，而且重点关注它们之间的相互作用。
 * 通过行为型模式，可以更加清晰地划分类与对象的职责，并研究系统在运行时实例对象之间的交互。行为型模式可以分为类行为型模式和对象行为型模式两种。
 * 职责链模式可以避免请求发送者与接收者耦合在一起，让多个对象都有可能接收请求，将这些对象连接成一条链，并且沿着这条链传递请求，直到有对象处理它为止。它是一种对象行为型模式。
 * 职责链模式包含两个角色：抽象处理者定义了一个处理请求的接口；具体处理者是抽象处理者的子类，它可以处理用户请求。
 * 在职责链模式里，很多对象由每一个对象对其下家的引用而连接起来形成一条链。请求在这个链上传递，直到链上的某一个对象决定处理此请求。
 * 发出这个请求的客户端并不知道链上的哪一个对象最终处理这个请求，这使得系统可以在不影响客户端的情况下动态地重新组织链和分配责任。
 * 职责链模式的主要优点在于可以降低系统的耦合度，简化对象的相互连接，同时增强给对象指派职责的灵活性，增加新的请求处理类也很方便；
 * 其主要缺点在于不能保证请求一定被接收，且对于比较长的职责链，请求的处理可能涉及到多个处理对象，系统性能将受到一定影响，而且在进行代码调试时不太方便。
 * 职责链模式适用情况包括：有多个对象可以处理同一个请求，具体哪个对象处理该请求由运行时刻自动确定；在不明确指定接收者的情况下，向多个对象中的一个提交
 * 一个请求；可动态指定一组对象处理请求。
 */
package designpattern.chain_of_responsibility.v2;

public abstract class Handler {
    
    protected Handler successor = null;
    
    public Handler getSuccessor(){
        return successor;
    }
    
    public void setSuccessor(Handler successor){
        this.successor = successor;
    }
    
    public abstract String handleRequest(String user, double fee);
}
