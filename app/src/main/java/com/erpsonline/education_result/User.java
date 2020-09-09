package com.erpsonline.education_result;

public class User {
    String user_id;
    public User(String id)
    {
        this.user_id=id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
