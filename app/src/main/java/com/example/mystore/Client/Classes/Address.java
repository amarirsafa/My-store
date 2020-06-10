package com.example.mystore.Client.Classes;

public class Address {
    String city,country,province,street;
    int postalCode;

    public Address(String city, String country, String province, String street, int postalCode) {
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

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Address" +
                "city:'" + city + ' ' +
                ", country:'" + country + ' ' +
                ", province:'" + province + ' ' +
                ", street:'" + street + ' ' +
                ", postalCode:" + postalCode;
    }
}
