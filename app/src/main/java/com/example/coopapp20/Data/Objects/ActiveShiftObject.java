package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity(tableName = "ActiveShifts", ignoredColumns = {"ShiftHolderName","DepartmentName","DepartmentId","DateString", "DayString","TimeString","Divider","SwitchActive","SwitchStatusColor","ResponseShiftSelected","ScheduleShiftChangeBtn"})
public class ActiveShiftObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private LocalDate Date;
    private LocalTime StartTime;
    private LocalTime EndTime;
    private Duration BreakTime;
    private Integer FirstShiftHolder;
    private Integer CurrentShiftHolder;
    private Integer ShiftId;

    public ActiveShiftObject(Integer Id, LocalDate Date, LocalTime StartTime, LocalTime EndTime, Duration BreakTime, Integer FirstShiftHolder, Integer CurrentShiftHolder, Integer ShiftId){
        this.Id = Id;
        this.Date = Date;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.BreakTime = BreakTime;
        this.FirstShiftHolder = FirstShiftHolder;
        this.CurrentShiftHolder = CurrentShiftHolder;
        this.ShiftId = ShiftId;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getId() { return Id; }
    public LocalDate getDate() { return Date; }
    public LocalTime getStartTime() { return StartTime; }
    public LocalTime getEndTime() { return EndTime; }
    public Duration getBreakTime() { return BreakTime; }
    public Integer getFirstShiftHolder() { return FirstShiftHolder; }
    public Integer getCurrentShiftHolder() { return CurrentShiftHolder; }
    public Integer getShiftId() { return ShiftId; }

    private Boolean ScheduleShiftChangeBtn;
    private String ShiftHolderName;
    private String DepartmentName;
    private Integer DepartmentId;
    private String DateString;
    private String DayString;
    private String TimeString;
    private Boolean Divider;
    private Boolean SwitchActive;
    private int SwitchStatusColor;
    private Boolean ResponseShiftSelected = false;

    public void setScheduleShiftChangeBtn(boolean show){ScheduleShiftChangeBtn = show;}
    public void setShiftHolderName(String shiftHolderName) { ShiftHolderName = shiftHolderName; }
    public void setDepartmentName(String departmentName) { DepartmentName = departmentName; }
    public void setDepartmentId(Integer departmentId) { DepartmentId = departmentId; }
    public void setDateString(String dateString) { DateString = dateString; }
    public void setDayString(String dayString) { DayString = dayString; }
    public void setTimeString(String timeString) { TimeString = timeString; }
    public void setDivider(Boolean divider) { Divider = divider; }
    public void setSwitchActive(Boolean switchActive) { SwitchActive = switchActive; }
    public void setSwitchStatusColor(int switchStatusColor) { SwitchStatusColor = switchStatusColor; }
    public void setResponseShiftSelected() { ResponseShiftSelected ^= true; }

    public Boolean getScheduleShiftChangeBtn() { return ScheduleShiftChangeBtn; }
    public String getShiftHolderName() { return ShiftHolderName; }
    public String getDepartmentName() { return DepartmentName; }
    public Integer getDepartmentId() { return DepartmentId; }
    public String getDateString() { return DateString; }
    public String getDayString() { return DayString; }
    public String getTimeString() { return TimeString; }
    public Boolean getDivider() { return Divider; }
    public Boolean getSwitchActive() { return SwitchActive; }
    public int getSwitchStatusColor() { return SwitchStatusColor; }
    public Boolean getResponseShiftSelected() { return ResponseShiftSelected; }
}
