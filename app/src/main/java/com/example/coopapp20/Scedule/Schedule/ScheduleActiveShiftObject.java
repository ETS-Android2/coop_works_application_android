package com.example.coopapp20.Scedule.Schedule;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ScheduleActiveShiftObject {

    public static int ICON_NON = 0;
    public static int ICON_REQUEST_BTN = 1;
    public static int ICON_REQUEST_ICON = 2;

    Integer CurrentUserId;
    Integer ShiftHolderId;
    String ShiftHolderName;
    String DepartmentName;
    LocalDate Date;
    LocalTime StartTime;
    LocalTime EndTime;
    Integer ActiveShiftId;
    Integer ShiftChangeRequestStatus;
    Integer Icon;

    //If activeshift is CurrentUsers and ShiftChangeRequest isn't made

    public ScheduleActiveShiftObject(Integer CurrentUserId, Integer ShiftHolderId, String ShiftHolderName, String DepartmentName,LocalDate Date, LocalTime StartTime, LocalTime EndTime, Integer ActiveShiftId, Integer ShiftChangeRequestStatus){
        this.CurrentUserId = CurrentUserId;
        this.ShiftHolderId = ShiftHolderId;
        this.ShiftHolderName = ShiftHolderName;
        this.DepartmentName = DepartmentName;
        this.Date = Date;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.ShiftChangeRequestStatus = ShiftChangeRequestStatus;
        this.ActiveShiftId = ActiveShiftId;
    }

    public String getShiftHolderName() {
        return ShiftHolderName;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public LocalDate getDate() {
        return Date;
    }

    public LocalTime getStartTime() {
        return StartTime;
    }

    public LocalTime getEndTime() {
        return EndTime;
    }

    public Integer getActiveShiftId() {
        return ActiveShiftId;
    }

    public Integer getIcon() {
        if(CurrentUserId.equals(ShiftHolderId) && ShiftChangeRequestStatus == null && !Date.isBefore(LocalDate.now())){
            Icon = ICON_REQUEST_BTN;
        }else if(CurrentUserId.equals(ShiftHolderId) || ShiftChangeRequestStatus == null && !Date.isBefore(LocalDate.now())){
            Icon = ICON_NON;
        }else {
            Icon = ICON_REQUEST_ICON;
        }

        return Icon;
    }
}
