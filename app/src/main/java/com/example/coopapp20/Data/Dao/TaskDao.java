package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.TaskObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insert(TaskObject task);

    @Update
    void update(TaskObject tasks);

    @Delete
    void delete(TaskObject tasks);

    @Query("DELETE FROM Tasks")
    void deleteAll();

    @Query("SELECT * FROM Tasks WHERE :taskId = Id")
    TaskObject getClean(int taskId);

    @Query("SELECT * FROM Tasks")
    LiveData<List<TaskObject>> getAll();

    @Query("SELECT * FROM Tasks WHERE Repeat = :repeat")
    LiveData<List<TaskObject>> getByRepeat(boolean repeat);

    @Query("SELECT * FROM Tasks WHERE DepartmentId = :departmentId")
    LiveData<List<TaskObject>> getByDepartment(Integer departmentId);

    @Query("SELECT * FROM Tasks WHERE DepartmentId = :departmentId")
    List<TaskObject> getByDepartmentClean(Integer departmentId);

    @Query("SELECT * FROM Tasks WHERE (Days - (:epochDaysNow - LastCompletionDate)) < 2 AND :departmentId = DepartmentId ")
    LiveData<List<TaskObject>> getCurrentUserTasks(Integer departmentId, LocalDate epochDaysNow);

}
