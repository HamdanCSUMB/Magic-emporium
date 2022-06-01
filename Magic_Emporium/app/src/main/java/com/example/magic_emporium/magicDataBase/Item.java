package com.example.magic_emporium.magicDataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = MagicEmporiumDatabase.ITEM_TABLE)
public class Item {
    /*
    What should an item have

    ID
    //if time permits we get an image as well
    Name
    category
    description
    price
    //if time permits maybe discounts? probably not though
    stock
     */

    @PrimaryKey(autoGenerate = true)
    private int uid;     //item id

    @ColumnInfo(name = "name")
    private String itemName;

    @ColumnInfo(name = "category")
    private String itemCategory;

    @ColumnInfo(name = "description")
    private String itemDescription;

    @ColumnInfo(name = "price")
    private double itemPrice;

    @ColumnInfo(name = "stock")
    private int itemStock;

    public Item(String itemName, String itemCategory, String itemDescription, double itemPrice, int itemStock) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemStock() {
        return itemStock;
    }

    public void setItemStock(int itemStock) {
        this.itemStock = itemStock;
    }

    @Override
    public String toString() {
        return "Item{" +
                "uid=" + uid +
                ", itemName='" + itemName + '\'' +
                ", itemCategory='" + itemCategory + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemStock=" + itemStock +
                '}';
    }

/*
    column
    user ids looking at this item

    look at how room does a many to one
    to

    add list

    cart
    list object
    a bunch of garbage
    get this




     */


}
