package com.example.password_manager.database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserPassword.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // Declare DAO
    public abstract UserPasswordDAO userPasswordDao();
}
