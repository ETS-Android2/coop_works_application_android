package com.example.coopapp20.Scedule.ScheduleShiftChange;

import androidx.room.Ignore;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleShiftChangeSwitchObject {

    private LocalDate ShiftDate;
    private Integer ShiftHolderId;
    private String ShiftHolderName;
    private LocalTime ShiftStartTime;
    private LocalTime ShiftEndTime;

    private Integer ResponseStatus;

    @Ignore
    private Integer ResponseStatusColor;

    public ScheduleShiftChangeSwitchObject(LocalDate ShiftDate, Integer ShiftHolderId, String ShiftHolderName, LocalTime ShiftStartTime, LocalTime ShiftEndTime, Integer ResponseStatus, Integer ResponseStatusColor){
        this.ShiftDate = ShiftDate;
        this.ShiftHolderId = ShiftHolderId;
        this.ShiftHolderName = ShiftHolderName;
        this.ShiftStartTime = ShiftStartTime;
        this.ShiftEndTime = ShiftEndTime;
        this.ResponseStatus = ResponseStatus;
        this.ResponseStatusColor = ResponseStatusColor;
    }

    public LocalDate getShiftDate() {
        return ShiftDate;
    }

    public Integer getShiftHolderId() {
        return ShiftHolderId;
    }

    public String getShiftHolderName() {
        return ShiftHolderName;
    }

    public LocalTime getShiftStartTime() {
        return ShiftStartTime;
    }

    public LocalTime getShiftEndTime() {
        return ShiftEndTime;
    }

    public Integer getResponseStatus() {
        return ResponseStatus;
    }

    public Integer getResponseStatusColor() {
        return ResponseStatusColor;
    }
}
