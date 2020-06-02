package com.example.mystore;

public class User {
    String name,email,CIN,address;
    Integer phoneNumber;
    Boolean admin;

    User(){};

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCIN() { return CIN; }
    public void setCIN(String CIN) { this.CIN = CIN; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(Integer phoneNumber) { this.phoneNumber = phoneNumber; }

    public Boolean getAdmin() { return admin; }
    public void setAdmin(Boolean admin) { this.admin = admin; }
}
