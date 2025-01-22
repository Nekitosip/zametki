package com.example.myzametki.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myzametki.Models.Zametki;

import java.util.List;

@Dao
public interface MainDAO {
    @Insert(onConflict = REPLACE)
    void insert (Zametki zametki);

    @Query("SELECT * FROM zametki ORDER BY id DESC")
    List<Zametki> getAll();

    @Query("UPDATE zametki SET title = :title, zametki = :zametki WHERE ID = :id")
    void update (int id, String title, String zametki);

    @Delete
    void delete (Zametki zametki);
    @Query("UPDATE zametki SET pinned = :pin WHERE ID = :id")
    void pin (int id, boolean pin);

}
