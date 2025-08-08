package com.example.keymasterdegozer.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {UserPassword.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    // Declare DAO
    public abstract UserPasswordDAO userPasswordDao();

}