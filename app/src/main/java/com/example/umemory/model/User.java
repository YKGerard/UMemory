package com.example.umemory.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by ${HYK} on 2017/3/26.
 */

public class User extends BmobObject{
    private String username;
    private String password;
    private String email;

    public User(){
        super();
    }

    public User(String email,String username,String password){
        this.email=email;
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
