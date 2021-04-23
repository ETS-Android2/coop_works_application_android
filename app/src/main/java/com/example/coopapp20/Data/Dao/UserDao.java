package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Contact.Contact.ContactUserObject;
import com.example.coopapp20.Data.Objects.UserObject;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    long insert(UserObject user);

    @Update()
    void update(UserObject user);

    @Delete()
    void delete(UserObject user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM Users")
    LiveData<List<UserObject>> getAll();

    @Query("SELECT * FROM Users WHERE DepartmentId = :departmentId")
    LiveData<List<UserObject>> getByDepartment(Integer departmentId);

    @Query("SELECT * FROM Users WHERE DepartmentId = :departmentId")
    List<UserObject> getByDepartmentClean(Integer departmentId);

    @Query("SELECT * FROM users WHERE :userId = Id")
    LiveData<UserObject> getUser(Integer userId);

    @Query("SELECT * FROM users WHERE :userId = Id")
    UserObject getUserClean(Integer userId);

    @Query("SELECT * FROM users WHERE :username = UserName COLLATE NOCASE AND :password == Password COLLATE NOCASE LIMIT 1")
    LiveData<UserObject> getUserByCredentials(String username, String password);

    @Query("SELECT U.Name UserName, U.Id UserId, U.Status UserStatus, D.Name DepartmentName, D.Color DepartmentColor FROM Users U " +
            "INNER JOIN Departments D ON D.Id = U.DepartmentId " +
            "WHERE :UserId = U.Id")
    List<ContactUserObject> getContactUsersByIdClean(Integer UserId);

    @Query("SELECT Id From Users user Where user.DepartmentId = (SELECT DepartmentId FROM Shifts shift WHERE shift.Id = (SELECT ShiftId FROM ActiveShifts activeShift WHERE activeShift.id = (SELECT ActiveShiftId FROM ShiftChangeRequests request WHERE request.id = :RequestId)) AND user.Id != (SELECT SenderId FROM ShiftChangeRequests request WHERE request.id = :RequestId))")
    List<Integer> getUserIdByShiftChangeRequestClean(Integer RequestId);
}
