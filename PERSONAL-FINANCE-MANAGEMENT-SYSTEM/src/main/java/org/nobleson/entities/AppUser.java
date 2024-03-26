package org.nobleson.entities;

import java.sql.Date;

public class AppUser {
    private Long userID;
    private String surname;
    private String otherName;
    private String username;
    private  String email;
    private Date createdAt;

    private String password;
    public AppUser(){}

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setOtherName(String othername) {
        this.otherName = othername;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setCreatedAt(Date created_at) {
        this.createdAt = created_at;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
