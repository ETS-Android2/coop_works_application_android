package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.DepartmentObject;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DepartmentDao {

    @Insert(onConflict = REPLACE)
    long insert(DepartmentObject users);

    @Update()
    void update(DepartmentObject users);

    @Delete()
    void delete(DepartmentObject users);

    @Query("DELETE FROM Departments")
    void deleteAll();

    @Query("SELECT * FROM Departments")
    LiveData<List<DepartmentObject>> getAll();

    @Query("SELECT * FROM Departments " +
            "WHERE :id is null OR Id = :id " +
            "AND :name is null OR Name = :name " +
            "AND :color is null OR Color = :color " +
            "")
    LiveData<List<DepartmentObject>> getEquals(Integer id, String name, Integer color);

    @Query("SELECT * FROM Departments " +
            "WHERE :id is null OR Id = :id " +
            "AND :name is null OR Name LIKE '%' || :name || '%' " +
            "AND :color is null OR Color = :color " +
            "")
    LiveData<List<DepartmentObject>> getContains(Integer id, String name, Integer color);

    @Query("SELECT * FROM Departments WHERE :departmentId == Id LIMIT 1" )
    LiveData<DepartmentObject> getDepartment(Integer departmentId);

    @Query("SELECT * FROM Departments WHERE :departmentId == null OR :departmentId == Id")
    LiveData<DepartmentObject> getByDepartment(Integer departmentId);
}
