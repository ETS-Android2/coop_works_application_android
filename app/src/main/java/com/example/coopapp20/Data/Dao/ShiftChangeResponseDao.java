package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.ShiftChangeResponseObject;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ScheduleShiftChangeResponseObject;

import java.util.List;

@Dao
public interface ShiftChangeResponseDao {

    @Insert
    long insert(ShiftChangeResponseObject shiftChangeResponse);

    @Update
    void update(ShiftChangeResponseObject shiftChangeResponse);

    @Delete
    void delete(ShiftChangeResponseObject shiftChangeResponse);

    @Query("DELETE FROM shiftChangeResponses")
    void deleteAll();

    @Query("SELECT * FROM shiftChangeResponses")
    LiveData<List<ShiftChangeResponseObject>> getAll();

    @Query("SELECT * FROM shiftChangeResponses WHERE ShiftChangeRequestId = :requestId ")
    LiveData<List<ShiftChangeResponseObject>> getByRequest(Integer requestId);

    @Query("SELECT * FROM shiftChangeResponses WHERE ShiftChangeRequestId = :requestId ")
    List<ShiftChangeResponseObject> getByRequestClean(Integer requestId);

    @Query("SELECT * FROM shiftChangeResponses WHERE ResponderId = :userId ")
    List<ShiftChangeResponseObject> getByUserClean(Integer userId);

    @Query("SELECT * FROM shiftChangeResponses WHERE ResponderId = :UserId AND ShiftChangeRequestId = :requestId LIMIT 1")
    LiveData<ShiftChangeResponseObject> getByUserRequest(Integer UserId, Integer requestId);

    @Query("SELECT * FROM shiftChangeResponses WHERE ResponderId = :UserId AND ShiftChangeRequestId = :requestId LIMIT 1")
    ShiftChangeResponseObject getByUserRequestClean(Integer UserId, Integer requestId);

   @Query("SELECT responder.Name ResponderName, response.ResponderId ResponderId, response.Status ResponseStatus, response.ShiftIdList ResponseShiftIdList, response.Id ResponseId FROM ShiftChangeResponses response " +
           "LEFT JOIN Users responder ON responder.Id = response.ResponderId " +
           "WHERE response.ShiftChangeRequestId = :RequestId")
    LiveData<List<ScheduleShiftChangeResponseObject>> getScheduleShiftChangeResponseObjects(Integer RequestId);
}
