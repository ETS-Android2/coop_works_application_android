package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.Scedule.Schedule.ScheduleActiveShiftObject;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ScheduleShiftChangeSwitchObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Dao
public interface ActiveShiftDao {

    @Insert
    long insert(ActiveShiftObject activeShifts);

    @Update
    void update(ActiveShiftObject activeShifts);

    @Delete
    void delete(ActiveShiftObject activeShifts);

    @Query("DELETE FROM activeShifts")
    void deleteAll();

    @Query("SELECT * FROM activeShifts")
    LiveData<List<ActiveShiftObject>> getAll();

    @Query("SELECT * FROM activeShifts WHERE Id = :id LIMIT 1")
    LiveData<ActiveShiftObject> getById(Integer id);

    @Query("SELECT * FROM activeShifts WHERE Id = :id LIMIT 1")
    ActiveShiftObject getByIdClean(Integer id);

    @Query("SELECT * FROM activeShifts WHERE Date > :date")
    LiveData<List<ActiveShiftObject>> getAfter(LocalDate date);

    @Query("SELECT * FROM activeShifts " +
            "WHERE CurrentShiftHolder = :UserId " +
            "AND Date = :date " +
            "LIMIT 1")
    LiveData<ActiveShiftObject> getCurrentActiveShift(Integer UserId, LocalDate date);

    @Query("SELECT * FROM activeShifts WHERE CurrentShiftHolder = :UserId")
    List<ActiveShiftObject> getByUserClean(Integer UserId);

    @Query("SELECT * FROM activeShifts " +
            "WHERE CurrentShiftHolder = :UserId " +
            "AND Date > :date " +
            "LIMIT 1")
    LiveData<List<ActiveShiftObject>> getByUserIdAndAfter(Integer UserId, LocalDate date);

    @Query("SELECT * FROM activeShifts " +
            "WHERE CurrentShiftHolder = :UserId " +
            "AND Date > :date ")
    List<ActiveShiftObject> getByUserIdAndAfterClean(Integer UserId, LocalDate date);

    @Query("SELECT :CurrentUserId CurrentUserId, CurrentShiftHolder ShiftHolderId, user.Name ShiftHolderName, department.Name DepartmentName, Date, StartTime, EndTime, activeShift.Id ActiveShiftId, request.Status ShiftChangeRequestStatus FROM ActiveShifts activeShift " +

            "LEFT JOIN Users user ON user.Id = activeShift.CurrentShiftHolder " +
            "LEFT JOIN Departments department ON department.Id = user.DepartmentId " +
            "LEFT JOIN ShiftChangeRequests request ON (request.SenderId = user.Id AND request.ActiveShiftId = activeShift.Id) " +

            "WHERE Date >= :StartDate " +
            "AND Date < :EndDate " +
            "AND CurrentShiftHolder == :UserId " +

            "OR Date >= :StartDate " +
            "AND Date < :EndDate " +
            "AND :UserId IS null " +
            "AND CurrentShiftHolder IN (SELECT Id FROM Users WHERE DepartmentId = :DepartmentId)" +

            "OR Date >= :StartDate " +
            "AND Date < :EndDate " +
            "AND :UserId IS null " +
            "AND :DepartmentId IS null")
    LiveData<List<ScheduleActiveShiftObject>> getScheduleActiveShifts(LocalDate StartDate, LocalDate EndDate, Integer DepartmentId, Integer UserId, Integer CurrentUserId);

    //@Query("SELECT activeShift.Date, activeShift.CurrentShiftHolder, user.Name, shift.StartTime, shift.EndTime, response.Status FROM ShiftChangeResponses response " +
    //        "LEFT JOIN ActiveShifts activeShift ON activeShift.CurrentShiftHolder = response.ResponderId " +
    //        "LEFT JOIN Shifts shift ON activeShift.ShiftId = shift.Id " +
    //        "LEFT JOIN Users user ON user.Id = response.ResponderId " +
    //        "WHERE response.ShiftChangeRequestId = :RequestId AND response.Status ")
    //LiveData<List<ScheduleShiftChangeSwitchObject>> getShiftChangeRequestSwitches(Integer RequestId, Integer UserId, LocalDate Date);

}
