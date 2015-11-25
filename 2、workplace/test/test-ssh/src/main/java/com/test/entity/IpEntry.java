package com.test.entity;



/**
 * IpEntry entity. @author MyEclipse Persistence Tools
 */
public class IpEntry extends AbstractIpEntry implements java.io.Serializable {

    // Constructors

    private static final long serialVersionUID = -853770956836465446L;


    /** default constructor */
    public IpEntry() {
    }

    
    /** full constructor */
    public IpEntry(String ipBegin, String ipEnd, Long locationId) {
        super(ipBegin, ipEnd, locationId);        
    }
   
}
