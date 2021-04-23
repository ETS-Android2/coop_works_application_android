package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.ProductChangeObject;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface ProductChangeDao {

    @Insert
    long insert(ProductChangeObject ProductChange);

    @Update
    void update(ProductChangeObject ProductChange);

    @Delete
    void delete(ProductChangeObject ProductChange);

    @Query("DELETE FROM ProductChanges")
    void deleteAll();

    @Query("SELECT * FROM ProductChanges")
    LiveData<List<ProductChangeObject>> getAll();

    @Query("SELECT * FROM ProductChanges " +
            "WHERE :id is null OR Id = :id " +
            "AND :type is null OR ChangeType = :type " +
            "AND :productid is null OR ProductId = :productid " +
            "AND :dateTime is null OR DateTime = :dateTime " +
            "")
    LiveData<List<ProductChangeObject>> getEquals(Integer id, Integer type, Integer productid, LocalDateTime dateTime);

}
