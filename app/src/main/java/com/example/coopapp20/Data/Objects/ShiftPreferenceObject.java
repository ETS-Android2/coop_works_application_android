package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Duration;
import java.time.LocalTime;

@Entity(tableName = "ShiftPreferences", ignoredColumns = {"DayString","TimeString","PrefColor"})
public class ShiftPreferenceObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    public static final int VALUE_UNABLE = 0;
    public static final int VALUE_UNWILLING = 1;
    public static final int VALUE_INDIFFERENT = 2;
    public static final int VALUE_WANTED = 3;


    private Integer Day;
    private LocalTime StartTime;
    private LocalTime EndTime;
    private Integer Value;
    private Integer ShiftId;
    private Integer UserId;

    public ShiftPreferenceObject(Integer Id, Integer Day, LocalTime StartTime, LocalTime EndTime, Integer Value, Integer ShiftId, Integer UserId){
        this.Id = Id;
        this.Day = Day;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Value = Value;
        this.ShiftId = ShiftId;
        this.UserId = UserId;
    }

    public void setId(Integer id) { Id = id; }

    public Integer getId() { return Id; }
    public Integer getDay() { return Day; }
    public LocalTime getStartTime() { return StartTime; }
    public LocalTime getEndTime() { return EndTime; }
    public Integer getValue() { return Value; }
    public Integer getShiftId() { return ShiftId; }
    public Integer getUserId() { return UserId; }

    public void setValue(Integer value) {Value = value;}

    public void setDay(Integer day) { Day = day; }
    public void setStartTime(LocalTime startTime) { StartTime = startTime; }
    public void setEndTime(LocalTime endTime) { EndTime = endTime; }

    private String DayString;
    private String TimeString;
    private int PrefColor;

    public void setExtraValues(String dayString, String timeString, int prefColor){
        DayString = dayString;
        TimeString = timeString;
        PrefColor = prefColor;
    }

    public String getDayString() { return DayString; }
    public String getTimeString() { return TimeString; }
    public int getPrefColor() { return PrefColor; }
}
