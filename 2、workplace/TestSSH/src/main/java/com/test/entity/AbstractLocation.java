package com.test.entity;



/**
 * AbstractLocation entity provides the base persistence definition of the Location entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractLocation  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String countryId;
     private String country;
     private Integer provinceId;
     private String province;
     private Integer cityId;
     private String city;
     private Integer districtId;
     private String district;
     private Double lat;
     private Double lng;


    // Constructors

    /** default constructor */
    public AbstractLocation() {
    }

	/** minimal constructor */
    public AbstractLocation(Long id) {
        this.id = id;
    }
    
    /** full constructor */
    public AbstractLocation(Long id, String countryId, String country, Integer provinceId, String province, Integer cityId, String city, Integer districtId, String district, Double lat, Double lng) {
        this.id = id;
        this.countryId = countryId;
        this.country = country;
        this.provinceId = provinceId;
        this.province = province;
        this.cityId = cityId;
        this.city = city;
        this.districtId = districtId;
        this.district = district;
        this.lat = lat;
        this.lng = lng;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryId() {
        return this.countryId;
    }
    
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return this.country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getProvinceId() {
        return this.provinceId;
    }
    
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return this.province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getCityId() {
        return this.cityId;
    }
    
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }

    public Integer getDistrictId() {
        return this.districtId;
    }
    
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrict() {
        return this.district;
    }
    
    public void setDistrict(String district) {
        this.district = district;
    }

    public Double getLat() {
        return this.lat;
    }
    
    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return this.lng;
    }
    
    public void setLng(Double lng) {
        this.lng = lng;
    }
   








}