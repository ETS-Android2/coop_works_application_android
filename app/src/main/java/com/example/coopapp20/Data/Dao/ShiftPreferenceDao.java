package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.ShiftPreferenceObject;
import com.example.coopapp20.Scedule.SchedulePref.ScheduleShiftPreferenceObject;

import java.util.List;

@Dao
public interface ShiftPreferenceDao {

    @Insert
    long insert(ShiftPreferenceObject shiftPreferences);

    @Update
    void update(ShiftPreferenceObject shiftPreferences);

    @Delete
    void delete(ShiftPreferenceObject shiftPreferences);

    @Query("DELETE FROM tasks")
    void deleteAll();

    @Query("SELECT * FROM shiftPreferences")
    LiveData<List<ShiftPreferenceObject>> getAll();

    @Query("SELECT * FROM shiftPreferences WHERE Id = :Id")
    ShiftPreferenceObject getByIdClean(Integer Id);

    @Query("SELECT * FROM shiftPreferences WHERE UserId = :userId")
    LiveData<List<ShiftPreferenceObject>> getByUser(Integer userId);

    @Query("SELECT * FROM shiftPreferences WHERE UserId = :userId")
    List<ShiftPreferenceObject> getByUserClean(Integer userId);

    @Query("SELECT * FROM shiftPreferences WHERE ShiftId = :shiftId")
    List<ShiftPreferenceObject> getByShiftClean(Integer shiftId);

    @Query("SELECT shift.Day Day, shift.StartTime StartTime, shift.EndTime EndTime, preferences.Value Value, preferences.Id Id FROM ShiftPreferences preferences " +
            "LEFT JOIN Shifts shift ON shift.Id = ShiftId " +
            "WHERE UserId = :UserId " +
            "ORDER BY shift.Day ASC")
    LiveData<List<ScheduleShiftPreferenceObject>> getScheduleShiftPreferences(Integer UserId);

}
