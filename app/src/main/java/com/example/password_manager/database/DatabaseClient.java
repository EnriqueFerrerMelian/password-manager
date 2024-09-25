package com.example.password_manager.database;
import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {
    private static DatabaseClient instance;
    private final AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        // Crear la instancia de la base de datos
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "UserPasswordDatabase").build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
