package com.sever.coinsdetector.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CoinDao {
    @Nullable
    @Query("SELECT * FROM coin")
    List<Coin> getAll();

    @Nullable
    @Query("SELECT * FROM coin WHERE name = :name")
    List<Coin> getByName(@NonNull String name);

    @Nullable
    @Query("SELECT * FROM coin WHERE name = :name AND year =:year")
    Coin getByNameAndYear(@NonNull String name, @NonNull String year);
}
