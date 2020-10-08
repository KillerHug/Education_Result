package com.hopeindia;

public class User {
    String user_name,name,paid,email,address;

    public User(String user_name,String name, String paid) {
        this.user_name = user_name;
        this.name = name;
        this.paid=paid;
    }

    public User(String txtEmail, String txtAddress) {
        this.email=txtEmail;
        this.address=txtAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
