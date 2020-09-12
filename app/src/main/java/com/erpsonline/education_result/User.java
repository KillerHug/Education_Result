package com.erpsonline.education_result;

public class User {
    String user_name,name;

    public User(String user_name,String name) {
        this.user_name = user_name;
        this.name = name;
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
