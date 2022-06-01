package com.example.magic_emporium.magicDataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BillingDao {
    /*
    what do we want to be able to get from that database

    update
    insert
    delete

    all billing history's   //for admin use //maybe order this by date?
    billing history of a specific user id   //maybe order this by date

     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Billing... billings);

    @Delete
    void delete(Billing billing);   //only admins will be able to delete billing history

    @Query("SELECT * FROM "+ MagicEmporiumDatabase.BILLING_TABLE+" WHERE :user_id = user_id ORDER BY date DESC")     //users specific billing history
    List<Billing> getUserBillingHistory(int user_id);

    @Query("SELECT * FROM "+MagicEmporiumDatabase.BILLING_TABLE)
    List<Billing>getAllBillingData();
}
