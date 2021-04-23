package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.ShiftChangeRequestObject;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ScheduleShiftChangeRequestObject;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface ShiftChangeRequestDao {

    @Insert
    long insert(ShiftChangeRequestObject shiftChangeRequest);

    @Update
    void update(ShiftChangeRequestObject shiftChangeRequest);

    @Delete
    void delete(ShiftChangeRequestObject shiftChangeRequest);

    @Query("DELETE FROM shiftchangerequests WHERE :RequestId = id")
    void deleteById(Integer RequestId);

    @Query("DELETE FROM shiftChangeRequests")
    void deleteAll();

    @Query("SELECT * FROM shiftChangeRequests")
    LiveData<List<ShiftChangeRequestObject>> getAll();

    @Query("SELECT * FROM shiftChangeRequests")
    List<ShiftChangeRequestObject> getAllClean();

    @Query("SELECT * FROM shiftchangerequests WHERE :Id = Id")
    ShiftChangeRequestObject getByIdClean(Integer Id);

    @Query("SELECT * FROM shiftChangeRequests WHERE SenderId = :userId")
    List<ShiftChangeRequestObject> getBySenderClean(Integer userId);

    @Query("SELECT * FROM shiftChangeRequests request WHERE (SELECT DepartmentId FROM USERS user WHERE user.Id = :userId) = (SELECT DepartmentId FROM Shifts shift WHERE shift.Id = (SELECT ShiftId FROM ActiveShifts activeshift WHERE activeshift.Id = request.ActiveShiftId))")
    List<ShiftChangeRequestObject> getByReceiverClean(int userId);

    @Query("SELECT sender.Name ShiftHolderName, receiver.Name ResponderName, activeshift.Date ShiftDate, shift.StartTime ShiftStartTime, shift.EndTime ShiftEndTime, request.Comment RequestComment, request.Status RequestStatus, request.Necessity RequestNecessity, request.SenderId RequestSenderId, request.Id RequestId FROM ShiftChangeRequests request " +
            "LEFT JOIN Users sender ON sender.Id = request.SenderId " +
            "LEFT JOIN Users receiver ON receiver.Id = (SELECT ResponderId FROM ShiftChangeResponses response WHERE response.ShiftChangeRequestId = request.Id AND response.Status IN (3, 4)) "+
            "LEFT JOIN ActiveShifts activeshift ON activeshift.Id = request.ActiveShiftId " +
            "LEFT JOIN Shifts shift ON shift.Id = activeshift.ShiftId ")
    LiveData<List<ScheduleShiftChangeRequestObject>> getScheduleShiftChangeRequests();

    @Query("SELECT sender.Name ShiftHolderName, receiver.Name ResponderName, activeshift.Date ShiftDate, shift.StartTime ShiftStartTime, shift.EndTime ShiftEndTime, request.Comment RequestComment, request.Status RequestStatus, request.Necessity RequestNecessity, request.SenderId RequestSenderId, request.Id RequestId FROM ShiftChangeRequests request " +
            "LEFT JOIN Users sender ON sender.Id = request.SenderId " +
            "LEFT JOIN Users receiver ON receiver.Id = (SELECT ResponderId FROM ShiftChangeResponses response WHERE response.ShiftChangeRequestId = request.Id AND response.Status IN (3, 4)) "+
            "LEFT JOIN ActiveShifts activeshift ON activeshift.Id = request.ActiveShiftId " +
            "LEFT JOIN Shifts shift ON shift.Id = activeshift.ShiftId " +
            "WHERE request.Id = :RequestId ")
    LiveData<ScheduleShiftChangeRequestObject> getScheduleShiftChangeRequestsById(Integer RequestId);

    @Query("SELECT Id FROM Users WHERE DepartmentId = (SELECT DepartmentId FROM Shifts shift WHERE shift.id = (SELECT ShiftId FROM ActiveShifts activeShift WHERE activeShift.Id = (SELECT ActiveShiftId FROM ShiftChangeRequests request WHERE request.Id = :RequestId))) AND Id != (SELECT SenderId FROM ShiftChangeRequests request WHERE request.Id = :RequestId)")
    List<Integer> getRelevantUserIds(Integer RequestId);
}
