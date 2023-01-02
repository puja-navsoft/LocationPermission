package com.muve.muve_it_driver.model;

public class CountryList {

    private String country;
    private int code;

    public CountryList(String country, int code) {
        this.country = country;
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
