package com.test.entity;



/**
 * IpEntry entity. @author MyEclipse Persistence Tools
 */
public class IpEntry extends AbstractIpEntry implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public IpEntry() {
    }

    
    /** full constructor */
    public IpEntry(String ipBegin, String ipEnd, Long locationId) {
        super(ipBegin, ipEnd, locationId);        
    }
   
}
