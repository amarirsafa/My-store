package com.example.mystore.Client.Classes;

public class Address {
    String city,country,province,street,postalCode;

    public Address(){}

    public Address(String city, String country, String province, String street, String postalCode) {
        this.city = city;
        this.country = country;
        this.province = province;
        this.street = street;
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return street+","+city+","+province+","+postalCode+","+country;
    }
}
