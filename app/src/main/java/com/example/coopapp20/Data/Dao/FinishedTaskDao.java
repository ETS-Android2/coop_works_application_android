package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.FinishedTaskObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Dao
public interface FinishedTaskDao {

    @Insert()
    long insert(FinishedTaskObject finishedtask);

    @Update()
    void update(FinishedTaskObject finishedtask);

    @Delete()
    void delete(FinishedTaskObject finishedtask);

    @Query("DELETE FROM FinishedTasks")
    void deleteAll();

    @Query("SELECT * FROM FinishedTasks")
    LiveData<List<FinishedTaskObject>> getAll();

    @Query("SELECT * FROM FinishedTasks WHERE UserId = :userId")
    LiveData<List<FinishedTaskObject>> getByUser(Integer userId);

    @Query("SELECT * FROM FinishedTasks WHERE UserId = :userId AND CompletionDate = :date")
    LiveData<List<FinishedTaskObject>> getByUserDate(Integer userId, LocalDate date);

}
