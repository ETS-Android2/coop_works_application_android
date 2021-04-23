package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.SchedulePreferenceObject;

import java.util.List;

@Dao
public interface SchedulePreferenceDao {

    @Insert
    long insert(SchedulePreferenceObject schedulePreferences);

    @Update
    void update(SchedulePreferenceObject schedulePreferences);

    @Delete
    void delete(SchedulePreferenceObject schedulePreferences);

    @Query("DELETE FROM schedulePreferences")
    void deleteAll();

    @Query("SELECT * FROM schedulePreferences")
    LiveData<List<SchedulePreferenceObject>> getAll();

    @Query("SELECT * FROM schedulePreferences WHERE UserId = :userId")
    LiveData<SchedulePreferenceObject> getByUser(Integer userId);

    @Query("SELECT * FROM schedulePreferences WHERE UserId = :userId")
    SchedulePreferenceObject getByUserClean(Integer userId);

    @Query("SELECT * FROM schedulePreferences " +
            "WHERE :id is null OR Id = :id " +
            "AND :prefDays is null OR PrefDays = :prefDays " +
            "AND :maxDays is null OR MaxDays = :maxDays " +
            "AND :userId is null OR UserId= :userId " +
            "")
    LiveData<List<SchedulePreferenceObject>> getEquals(Integer id, Integer prefDays, Integer maxDays, Integer userId);
}
