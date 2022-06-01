package com.example.magic_emporium.magicDataBase;
/*
Name:Hamdan
Date:12/1/2021
Description: User Data Access Object, dedicated for User data. current allows me to add,delete, and update a user
as well as allowing me to look for a user by their id, login, and password, the password needs to be changed later to get a list
so that matching passwords don't get wonky
 */

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    //what kind of actions will we need to make with our database
    //what do we want to be able to do
    /*
    add a new user
    delete a user
    update a user
    get all users login             //to check if any other user has the same login as a new user being created
    get all USERS themselves for the admins to look at
    find a user by login
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... users);

    @Update
    void update(User user); //only one user will be updated at any given time

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + MagicEmporiumDatabase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + MagicEmporiumDatabase.USER_TABLE + " WHERE uid = :user_id")   //a specific User with that id
    User findByUserId(int user_id);

    @Query("SELECT * FROM " + MagicEmporiumDatabase.USER_TABLE + " WHERE login = :user_login")   //a specific User with that login
    User findByLogin(String user_login);

    @Query("SELECT * FROM " + MagicEmporiumDatabase.USER_TABLE + " WHERE password = :user_password") //a specific User with that password
    User getUserByPassword(String user_password);   //need to change this to a list later


}
