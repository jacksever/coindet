package com.sever.coinsdetector;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sever.coinsdetector.database.Database;
import com.sever.coinsdetector.utils.Storage;

public class App extends Application {
    public static App instance;
    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, Database.class, "coins")
                .createFromAsset("database/coins")
                .allowMainThreadQueries()
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration()
                .build();

        Storage.SETTINGS = getSharedPreferences(Storage.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @NonNull
    public static App getInstance() {
        return instance;
    }

    @NonNull
    public Database getDatabase() {
        return database;
    }
}
