package com.unosquare.myfirtsapplication;

/**
 * Created by admin on 11/10/2014.
 */
public class Model {
    String username;
    String address;
    String phone;

    public Model() { }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
