package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.ScheduleValueObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Dao
public interface ScheduleValueDao {

    @Insert
    long insert(ScheduleValueObject scheduleValues);

    @Update
    void update(ScheduleValueObject scheduleValues);

    @Delete
    void delete(ScheduleValueObject scheduleValues);

    @Query("DELETE FROM ScheduleValues")
    void deleteAll();

    @Query("SELECT * FROM ScheduleValues")
    LiveData<List<ScheduleValueObject>> getAll();

    @Query("SELECT * FROM ScheduleValues")
    List<ScheduleValueObject> getAllClean();

    @Query("SELECT * FROM ScheduleValues WHERE DepartmentId = :departmentId ")
    LiveData<ScheduleValueObject> getByDepartment(Integer departmentId);

    @Query("SELECT * FROM ScheduleValues WHERE DepartmentId = :departmentId ")
    ScheduleValueObject getByDepartmentClean(Integer departmentId);

    @Query("SELECT * FROM ScheduleValues " +
            "WHERE :id is null OR Id = :id " +
            "AND :PreferenceDeadline is null OR PreferenceDeadline = :PreferenceDeadline " +
            "AND :CreationDeadline is null OR CreationDeadline = :CreationDeadline " +
            "AND :ScheduleBeginning is null OR ScheduleBeginning= :ScheduleBeginning " +
            "AND :PrefValueOnePoints is null OR PrefValueOnePoints = :PrefValueOnePoints " +
            "AND :PrefValueTwoPoints is null OR PrefValueTwoPoints = :PrefValueTwoPoints " +
            "AND :PrefValueThreePoints is null OR PrefValueThreePoints = :PrefValueThreePoints " +
            "AND :PrefWorkDaysDistPercent is null OR PrefWorkDaysDistPercent = :PrefWorkDaysDistPercent " +
            "AND :MedianPointsDistPercent is null OR MedianPointsDistPercent = :MedianPointsDistPercent " +
            "AND :DepartmentId is null OR DepartmentId = :DepartmentId " +
            "")
    LiveData<List<ScheduleValueObject>> getEquals(Integer id, LocalDate PreferenceDeadline, LocalTime CreationDeadline, LocalTime ScheduleBeginning, Integer PrefValueOnePoints, Integer PrefValueTwoPoints, Integer PrefValueThreePoints, Integer PrefWorkDaysDistPercent, Integer MedianPointsDistPercent, Integer DepartmentId);
}
