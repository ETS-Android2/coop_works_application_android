package com.example.coopapp20.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coopapp20.Data.Objects.TaskSortingValueObject;

import java.util.List;

@Dao
public interface TaskSortingValueDao {

    @Insert
    long insert(TaskSortingValueObject taskSortingValues);

    @Update
    void update(TaskSortingValueObject taskSortingValues);

    @Delete
    void delete(TaskSortingValueObject taskSortingValues);

    @Query("DELETE FROM TaskSortingValues")
    void deleteAll();

    @Query("SELECT * FROM TaskSortingValues")
    LiveData<List<TaskSortingValueObject>> getAll();

    @Query("SELECT * FROM TaskSortingValues WHERE DepartmentId = :departmentId ")
    TaskSortingValueObject getByDepartmentClean(Integer departmentId);

    @Query("SELECT * FROM TaskSortingValues " +
            "WHERE :id is null OR Id = :id " +
            "AND :taskPercentageLeftFactor is null OR TaskPercentageLeftFactor = :taskPercentageLeftFactor " +
            "AND :taskDaysLeftFactor is null OR TaskDaysLeftFactor = :taskDaysLeftFactor " +
            "AND :taskDoneDividingFactor is null OR TaskDoneDividingFactor= :taskDoneDividingFactor " +
            "AND :singleTaskValueOnePoints is null OR SingleTaskValueOnePoints = :singleTaskValueOnePoints " +
            "AND :singleTaskValueTwoPoints is null OR SingleTaskValueTwoPoints = :singleTaskValueTwoPoints " +
            "AND :repeatTaskValueOnePoints is null OR RepeatTaskValueOnePoints = :repeatTaskValueOnePoints " +
            "AND :repeatTaskValueTwoPoints is null OR RepeatTaskValueTwoPoints = :repeatTaskValueTwoPoints " +
            "AND :departmentId is null OR DepartmentId = :departmentId " +
            "")
    LiveData<List<TaskSortingValueObject>> getEquals(Integer id, Integer taskPercentageLeftFactor, Integer taskDaysLeftFactor, Integer taskDoneDividingFactor, Integer singleTaskValueOnePoints, Integer singleTaskValueTwoPoints, Integer repeatTaskValueOnePoints, Integer repeatTaskValueTwoPoints, Integer departmentId);

}
