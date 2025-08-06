package com.example.keymasterdegozer.database;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseClient {
    @SuppressLint("StaticFieldLeak")
    private static DatabaseClient instance;
    private final AppDatabase appDatabase;
    private Context context;

    /**
     * Añadir .fallbackToDestructiveMigration() para recrear la bd desde 0
     * @param context
     */
    private DatabaseClient(Context context) {
        // Create instance of the database
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "UserPasswordDatabase")
                .build();
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
    /**
     * MIGRACIÓN
     * Añadir si se modifica la app una vez publicada
     */
    /*private DatabaseClient(Context context) {
        this.context = context;

        // 🔥 Definir la migración 1 -> 2
        Migration MIGRATION_1_2 = new Migration(2, 3) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {
                // Crear una nueva tabla sin la columna 'icon'
                database.execSQL("CREATE TABLE userpassword_new (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "platform TEXT, " +
                        "account TEXT, " +
                        "password TEXT)");

                // Copiar los datos desde la antigua
                database.execSQL("INSERT INTO userpassword_new (id, platform, account, password) " +
                        "SELECT id, platform, account, password FROM user_passwords");

                // Borrar la antigua
                database.execSQL("DROP TABLE user_passwords");

                // Renombrar la nueva
                database.execSQL("ALTER TABLE userpassword_new RENAME TO userpasswords");
            }
        };

        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "my_database")
                .addMigrations(MIGRATION_1_2)
                .build();
    }*/

}
