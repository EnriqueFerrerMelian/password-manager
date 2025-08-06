package com.example.keymasterdegozer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserPasswordDAO {
    // Insert new entity and password
    @Insert
    void insert(UserPassword userPassword);

    // Delete an Entity
    @Delete
    void delete(UserPassword userPassword);

    // Edit an entity
    @Update
    void update(UserPassword userPassword);

    // Get password for a specific account
    @Query("SELECT * FROM user_passwords WHERE account = :account LIMIT 1")
    UserPassword getPasswordForAccount(String account);

    // Get all data
    @Query("SELECT * FROM user_passwords")
    List<UserPassword> getAllUserPasswords();


    // Order by Platform
    @Query("SELECT * FROM user_passwords ORDER BY platform ASC")
    List<UserPassword> getAllUserPasswordsByPlatform();

    // Order by entidad
    @Query("SELECT * FROM user_passwords ORDER BY account ASC")
    List<UserPassword> getAllUserPasswordsByAccount();

    // Order by id
    @Query("SELECT * FROM user_passwords ORDER BY id ASC")
    List<UserPassword> getAllUserPasswordsById();

    // Searchaccounts by platform using LIKE for partial coincidence
    @Query("SELECT * FROM user_passwords WHERE platform LIKE '%' || :platform || '%'")
    List<UserPassword> searchByPlatform(String platform);
}
