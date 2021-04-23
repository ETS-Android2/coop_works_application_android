package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

@Entity(tableName = "Shifts", ignoredColumns = {"DepartmentName","DayString","TimeString","DepartmentColor"})
public class ShiftObject {

    public static final int MONDAY = 0;
    public static final int TUESDAY = 1;
    public static final int WEDNESDAY = 2;
    public static final int THURSDAY = 3;
    public static final int FRIDAY = 4;
    public static final int SATURDAY = 5;
    public static final int SUNDAY = 6;


    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private Integer DepartmentId;
    private Integer Day;
    private LocalTime StartTime;
    private LocalTime EndTime;

    public ShiftObject(Integer Id, Integer DepartmentId, Integer Day, LocalTime StartTime, LocalTime EndTime){
        this.Id = Id;
        this.DepartmentId = DepartmentId;
        this.Day = Day;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
    }

    public void setId(Integer id) {
        Id = id;
    }
    public void setDay(Integer day) { Day = day; }
    public void setStartTime(LocalTime startTime) { StartTime = startTime; }
    public void setEndTime(LocalTime endTime) { EndTime = endTime; }

    public Integer getId() { return Id; }
    public Integer getDepartmentId() { return DepartmentId; }
    public Integer getDay() { return Day; }
    public LocalTime getStartTime() { return StartTime; }
    public LocalTime getEndTime() { return EndTime; }

    private String DepartmentName;
    private String DayString;
    private String TimeString;
    private Integer DepartmentColor;

    public void setDepartmentName(String departmentName) { DepartmentName = departmentName; }
    public void setDayString(String dayString) { DayString = dayString; }
    public void setTimeString(String timeString) { TimeString = timeString; }
    public void setDepartmentColor(Integer departmentColor) { DepartmentColor = departmentColor; }

    public String getDepartmentName() { return DepartmentName; }
    public String getDayString() { return DayString;}
    public String getTimeString() { return TimeString; }
    public Integer getDepartmentColor() { return DepartmentColor; }
}
