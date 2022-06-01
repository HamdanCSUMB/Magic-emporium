package com.example.magic_emporium.magicDataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = MagicEmporiumDatabase.BILLING_TABLE)
public class Billing {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    private int user_id;

    @ColumnInfo
    private String user_name;

    @ColumnInfo
    private String item_name;

    @ColumnInfo
    private int item_quantity;

    @ColumnInfo
    private double item_price;

    @ColumnInfo
    private Date date;

    public Billing(int user_id, String item_name, int item_quantity, double item_price, String user_name){
        this.user_id = user_id;
        this.item_name = item_name;
        this.item_quantity = item_quantity;
        this.item_price = item_price;
        this.date = new Date();
        this.user_name=user_name;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(int item_quantity) {
        this.item_quantity = item_quantity;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
