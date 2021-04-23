package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.ShiftObject;

import java.time.LocalTime;
import java.util.List;

@Dao
public interface ShiftDao {

    @Insert
    long insert(ShiftObject shift);

    @Update
    void update(ShiftObject shift);

    @Delete
    void delete(ShiftObject shift);

    @Query("DELETE FROM Shifts")
    void deleteAll();

    @Query("SELECT * FROM shifts")
    LiveData<List<ShiftObject>> getAll();

    @Query("SELECT * FROM shifts WHERE :DepartmentId == null OR DepartmentId == :DepartmentId ")
    LiveData<List<ShiftObject>> getByDepartment(Integer DepartmentId);

    @Query("SELECT * FROM shifts WHERE :DepartmentId == null OR DepartmentId == :DepartmentId ")
    List<ShiftObject> getByDepartmentClean(Integer DepartmentId);

    @Query("SELECT * FROM shifts " +
            "WHERE :id is null OR Id = :id " +
            "AND :departmentId is null OR DepartmentId = :departmentId " +
            "AND :day is null OR Day = :day " +
            "AND :startTime is null OR StartTime= :startTime " +
            "AND :endTime is null OR EndTime= :endTime " +
            "")
    LiveData<List<ShiftObject>> getEquals(Integer id, Integer departmentId, Integer day, LocalTime startTime, LocalTime endTime);


}
