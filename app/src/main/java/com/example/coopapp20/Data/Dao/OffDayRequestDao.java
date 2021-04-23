package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.OffDayRequestObject;
import com.example.coopapp20.Scedule.SchedulePref.ScheduleOffDayRequestObject;

import java.time.LocalTime;
import java.util.List;

@Dao
public interface OffDayRequestDao {

    @Insert
    long insert(OffDayRequestObject offDayRequest);

    @Update
    void update(OffDayRequestObject offDayRequest);

    @Delete
    void delete(OffDayRequestObject offDayRequest);

    @Query("DELETE FROM OffDayRequests WHERE Id = :Id ")
    void deleteById(Integer Id);

    @Query("DELETE FROM offDayRequests")
    void deleteAll();

    @Query("SELECT * FROM offDayRequests")
    LiveData<List<OffDayRequestObject>> getAll();

    @Query("SELECT * FROM offDayRequests WHERE Id = :Id")
    OffDayRequestObject getByIdClean(Integer Id);

    @Query("SELECT * FROM offDayRequests WHERE UserId = :userId")
    LiveData<List<OffDayRequestObject>> getByUser(Integer userId);

    @Query("SELECT Comment, Type, StartDate, EndDate, Status, Id FROM offDayRequests WHERE UserId = :userId")
    LiveData<List<ScheduleOffDayRequestObject>> getScheduleOffDayRequests(Integer userId);


}
