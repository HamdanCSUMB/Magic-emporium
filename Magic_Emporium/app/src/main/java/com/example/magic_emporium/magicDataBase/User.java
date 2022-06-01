package com.example.magic_emporium.magicDataBase;

/*
Name: Hamdan
Date: 12/2/2021
Description: POJO user object that I use as an entity. Serves to hold the users data and for passing an int of which user to be used when going to a new activity
by using putIntExtra
 */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = MagicEmporiumDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "login")
    private String login;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "is_admin")
    private boolean isAdmin;     //looks like java is automatically handling this as a 0 or 1 for sql in a class called UserDao_Impl
                                //so I may not have to deal with the whole sql can't take a bool thing
                                //future note, seems to be the case, user interactions and checking of admin when importing from sql are working fine

    @ColumnInfo(name = "payment_options")
    private String paymentOptions;          //this will be a string delimited with "," and "$"
                                            //, to seperate the different paymentOptions of the user
                                            //$ to seperate the values of the payment Option
    public User(String userName, String login, String password, boolean isAdmin){
        this.userName=userName;
        this.login=login;
        this.password=password;
        this.isAdmin=isAdmin;
        paymentOptions="";
    }

    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(String paymentOptions) {
        this.paymentOptions = paymentOptions;
    }
}