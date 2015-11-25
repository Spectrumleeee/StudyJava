package com.test.entity;



/**
 * AbstractIpEntry entity provides the base persistence definition of the IpEntry entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractIpEntry  implements java.io.Serializable {


    // Fields    

    private static final long serialVersionUID = -6466040144513028755L;
    private Long id;
     private String ipBegin;
     private String ipEnd;
     private Long locationId;


    // Constructors

    /** default constructor */
    public AbstractIpEntry() {
    }

    
    /** full constructor */
    public AbstractIpEntry(String ipBegin, String ipEnd, Long locationId) {
        this.ipBegin = ipBegin;
        this.ipEnd = ipEnd;
        this.locationId = locationId;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getIpBegin() {
        return this.ipBegin;
    }
    
    public void setIpBegin(String ipBegin) {
        this.ipBegin = ipBegin;
    }

    public String getIpEnd() {
        return this.ipEnd;
    }
    
    public void setIpEnd(String ipEnd) {
        this.ipEnd = ipEnd;
    }

    public Long getLocationId() {
        return this.locationId;
    }
    
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
   








}