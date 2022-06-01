package com.example.magic_emporium.magicDataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.magic_emporium.magicDataBase.DateTypeConverter.DateTypeConverter;
import com.example.magic_emporium.magicDataBase.User;
import com.example.magic_emporium.magicDataBase.UserDao;

@Database(entities = {User.class,Item.class,Billing.class}, version = 3)
@TypeConverters(DateTypeConverter.class)
public abstract class MagicEmporiumDatabase extends RoomDatabase {
    public static final String DB_NAME = "MAGICEMPORIUM_DATABASE";
    public static final String ITEM_TABLE ="ITEMTABLE";
    public static final String USER_TABLE ="USERTABLE";
    public static final String BILLING_TABLE ="BILLINGTABLE";

    public abstract UserDao userDao();
    public abstract ItemDao itemDao();
    public abstract BillingDao billingDao();
    //purchase history DAO
    //cart DAO

}
