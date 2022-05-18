package com.sever.coinsdetector.database;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Coin.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract CoinDao coinDao();
}
