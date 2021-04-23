package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.coopapp20.Data.Objects.DatabaseChangeObject;

import java.util.List;

@Dao
public interface DatabaseChangeDao {

    @Insert
    long insert(DatabaseChangeObject databaseChange);

    @Query("DELETE FROM DatabaseChange")
    void deleteAll();

    @Query("SELECT * FROM DatabaseChange")
    LiveData<List<DatabaseChangeObject>> getAll();

}
