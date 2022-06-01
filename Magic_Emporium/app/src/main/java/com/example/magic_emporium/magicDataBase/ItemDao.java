package com.example.magic_emporium.magicDataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    //what kind of actions will we need to make with our database
    //what do we want to be able to do
    /*
    add a new item
    delete a item
    update a item
    get all items for both users and admins to look at

    cart stuff? later
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item... items);

    @Update
    void update(Item item);//we will only update one item at a time

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM "+ MagicEmporiumDatabase.ITEM_TABLE)      //all items
    List<Item> getAll();

    @Query("SELECT * FROM "+ MagicEmporiumDatabase.ITEM_TABLE +" WHERE stock > 0  ORDER BY name DESC")
    List<Item> loadAllNonOOS();

    @Query("SELECT * FROM ITEMTABLE WHERE uid IN (:itemIds)")       //item by id, don't think I will need this, remove later if not used
    List<Item> loadAllByIds(int[] itemIds);





}
