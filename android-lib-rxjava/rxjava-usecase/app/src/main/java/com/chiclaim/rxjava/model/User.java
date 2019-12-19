package com.chiclaim.rxjava.model;

import java.util.List;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/9.
 */

public class User {

    private int id;
    private String username;
    private String email;

    private List<User> friends;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}
