package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.MessageObject;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface MessageDao {

    @Insert()
    long insert(MessageObject messages);

    @Update()
    void update(MessageObject messages);

    @Delete()
    void delete(MessageObject messages);

    @Query("DELETE FROM messages")
    void deleteAll();

    @Query("SELECT * FROM messages")
    LiveData<List<MessageObject>> getAll();

    @Query("SELECT * FROM messages WHERE Receiver = :departmentId")
    LiveData<List<MessageObject>> getByDepartment(Integer departmentId);

    @Query("SELECT * FROM messages WHERE Receiver = :departmentId")
    List<MessageObject> getByDepartmentClean(Integer departmentId);

    @Query("SELECT * FROM messages WHERE Receiver = :userId OR Sender = :userId")
    List<MessageObject> getByUserClean(Integer userId);

    @Query("SELECT * FROM messages WHERE " +
            "Sender = :userId OR " +
            "Receiver = :userId OR " +
            "Receiver = :departmentId")
    LiveData<List<MessageObject>> getUserInterActions(Integer userId, Integer departmentId);

    @Query("SELECT * FROM messages WHERE " +
            "Sender = :userId1 AND Receiver = :userId2 OR " +
            "Sender = :userId2 AND Receiver = :userId1 ")
    LiveData<List<MessageObject>> getByUsers(Integer userId1, Integer userId2);

}
