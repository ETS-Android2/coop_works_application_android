package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.ProductObject;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    long insert(ProductObject product);

    @Update
    void update(ProductObject product);

    @Delete
    void delete(ProductObject product);

    @Query("DELETE FROM products")
    void deleteAll();

    @Query("SELECT * FROM products")
    LiveData<List<ProductObject>> getAll();

    @Query("SELECT * FROM products " +
            "WHERE :id is null OR Id = :id " +
            "AND :name is null OR Name = :name " +
            "AND :manufacture is null OR Manufacture = :manufacture " +
            "AND :price is null OR Price = :price " +
            "AND :cost is null OR Cost = :cost " +
            "AND :discount is null OR Discount = :discount " +
            "AND :instore is null OR InStore = :instore " +
            "AND :information is null OR Information = :information " +
            "AND :barcode is null OR Barcode = :barcode " +
            "")
    LiveData<List<ProductObject>> getEquals(Integer id, String name, String manufacture, Float price, Float cost, String discount, Integer instore, String information, String barcode);



}
