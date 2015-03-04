package com.test.entity;



/**
 * Location entity. @author MyEclipse Persistence Tools
 */
public class Location extends AbstractLocation implements java.io.Serializable {

    // Constructors

    private static final long serialVersionUID = -5088385762800378433L;

    /** default constructor */
    public Location() {
    }

	/** minimal constructor */
    public Location(Long id) {
        super(id);        
    }
    
    /** full constructor */
    public Location(Long id, String countryId, String country, Integer provinceId, String province, Integer cityId, String city, Integer districtId, String district, Double lat, Double lng) {
        super(id, countryId, country, provinceId, province, cityId, city, districtId, district, lat, lng);        
    }
   
}
