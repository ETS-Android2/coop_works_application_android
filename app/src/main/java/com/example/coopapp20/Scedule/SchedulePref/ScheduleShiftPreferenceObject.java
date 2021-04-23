package com.example.coopapp20.Scedule.SchedulePref;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.coopapp20.Data.Objects.ShiftPreferenceObject;
import com.example.coopapp20.R;

import java.time.LocalTime;

public class ScheduleShiftPreferenceObject {

    private Integer Day;
    private LocalTime StartTime;
    private LocalTime EndTime;
    private Integer Value;
    private Integer Id;

    public ScheduleShiftPreferenceObject(Integer Day, LocalTime StartTime, LocalTime EndTime, Integer Value, Integer Id){
        this.Day = Day;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Value = Value;
        this.Id = Id;
    }

    public Integer getDay() {
        return Day;
    }

    public LocalTime getStartTime() {
        return StartTime;
    }

    public LocalTime getEndTime() {
        return EndTime;
    }

    public Integer getValue() {
        return Value;
    }

    public Integer getId() {
        return Id;
    }

    public Integer getColor(Context context){
       switch (Value){
           case ShiftPreferenceObject.VALUE_UNABLE: return ContextCompat.getColor(context, R.color.ShiftPrioritization0);
           case ShiftPreferenceObject.VALUE_UNWILLING: return ContextCompat.getColor(context, R.color.ShiftPrioritization1);
           case ShiftPreferenceObject.VALUE_INDIFFERENT: return ContextCompat.getColor(context, R.color.ShiftPrioritization2);
           case ShiftPreferenceObject.VALUE_WANTED: return ContextCompat.getColor(context, R.color.ShiftPrioritization3);
           default: return null;
       }
    }
}
