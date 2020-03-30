package com.openinstitute.nuru.Database;

public class User {
    private int userId;
    private String  user_email;
    private String user_fname;
    private String user_lname;
    private String user_number;
    private String user_password;
    private  String user_dp;
    private  String user_date;

    public User(int userId, String user_email, String user_fname, String user_lname, String user_number, String user_password) {
        this.userId = userId;
        this.user_email = user_email;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_number = user_number;
        this.user_password = user_password;
        this.user_dp = user_dp;
        this.user_date =  user_date;
    }

    public User() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_dp() {
        return user_dp;
    }

    public void setUser_dp(String user_dp) {
        this.user_dp = user_dp;
    }

    public String getUser_date() {
        return user_date;
    }

    public void setUser_date(String user_date) {
        this.user_date = user_date;
    }
}
