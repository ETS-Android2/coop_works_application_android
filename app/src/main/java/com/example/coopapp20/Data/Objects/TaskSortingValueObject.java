package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TaskSortingValues")
public class TaskSortingValueObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private Integer TaskPercentageLeftFactor;
    private Integer TaskDaysLeftFactor;
    private Integer TaskDoneDividingFactor;
    private Integer SingleTaskValueOnePoints;
    private Integer SingleTaskValueTwoPoints;
    private Integer RepeatTaskValueOnePoints;
    private Integer RepeatTaskValueTwoPoints;
    private Integer DepartmentId;

    public TaskSortingValueObject(Integer Id, Integer TaskPercentageLeftFactor, Integer TaskDaysLeftFactor, Integer TaskDoneDividingFactor, Integer SingleTaskValueOnePoints, Integer SingleTaskValueTwoPoints, Integer RepeatTaskValueOnePoints, Integer RepeatTaskValueTwoPoints, Integer DepartmentId){
        this.Id = Id;
        this.TaskPercentageLeftFactor = TaskPercentageLeftFactor;
        this.TaskDaysLeftFactor = TaskDaysLeftFactor;
        this.TaskDoneDividingFactor = TaskDoneDividingFactor;
        this.SingleTaskValueOnePoints = SingleTaskValueOnePoints;
        this.SingleTaskValueTwoPoints = SingleTaskValueTwoPoints;
        this.RepeatTaskValueOnePoints = RepeatTaskValueOnePoints;
        this.RepeatTaskValueTwoPoints = RepeatTaskValueTwoPoints;
        this.DepartmentId = DepartmentId;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getId() { return Id; }
    public Integer getTaskPercentageLeftFactor() { return TaskPercentageLeftFactor; }
    public Integer getTaskDaysLeftFactor() { return TaskDaysLeftFactor; }
    public Integer getTaskDoneDividingFactor() { return TaskDoneDividingFactor; }
    public Integer getSingleTaskValueOnePoints() { return SingleTaskValueOnePoints; }
    public Integer getSingleTaskValueTwoPoints() { return SingleTaskValueTwoPoints; }
    public Integer getRepeatTaskValueOnePoints() { return RepeatTaskValueOnePoints; }
    public Integer getRepeatTaskValueTwoPoints() { return RepeatTaskValueTwoPoints; }
    public Integer getDepartmentId() { return DepartmentId; }
}
