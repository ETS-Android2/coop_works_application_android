package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(tableName = "Tasks")
public class TaskObject {

    public static final int COMPLETE_IMMEDIATELY = 0;
    public static final int COMPLETE_SOON = 1;
    public static final int COMPLETE_BEFORE_DEADLINE = 2;
    public static final int PERFORM_OFTEN = 0;
    public static final int PERFORM_BEFORE_DEADLINE = 1;
    public static final int PERFORM_AD_DEADLINE = 2;

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private String Title;
    private Duration Duration;
    private LocalTime IdealExecutionTime;
    private Integer Days;
    private String Description;
    private Integer DepartmentId;
    private Integer Prioritization;
    private Boolean Repeat;
    private LocalDate LastCompletionDate;
    private LocalTime LastCompletionTime;

    public TaskObject(Integer Id, String Title, Duration Duration, LocalTime IdealExecutionTime, Integer Days, String Description, Integer DepartmentId, Integer Prioritization, Boolean Repeat, LocalDate LastCompletionDate, LocalTime LastCompletionTime){
        this.Id = Id;
        this.Title = Title;
        this.Duration = Duration;
        this.IdealExecutionTime = IdealExecutionTime;
        this.Days = Days;
        this.Description = Description;
        this.DepartmentId = DepartmentId;
        this.Prioritization = Prioritization;
        this.Repeat = Repeat;
        this.LastCompletionDate = LastCompletionDate;
        this.LastCompletionTime = LastCompletionTime;
    }

    public void setId(Integer id) {Id = id; }
    public void setTitle(String title){Title = title;}
    public void setDuration(Duration duration) {Duration = duration;}
    public void setIdealExecutionTime(LocalTime idealExecutionTime) { IdealExecutionTime = idealExecutionTime; }
    public void setDays(Integer days) { Days = days; }
    public void setDescription(String description) { Description = description; }
    public void setDepartmentId(Integer departmentId) { DepartmentId = departmentId; }
    public void setPrioritization(Integer prioritization) { Prioritization = prioritization;}
    public void setRepeat(Boolean repeat) { Repeat = repeat; }
    public void setLastCompletionDate(LocalDate dateTime){LastCompletionDate = dateTime;}
    public void setLastCompletionTime(LocalTime dateTime){LastCompletionTime = dateTime;}

    public Integer getId() {return Id;}
    public String getTitle() {return Title;}
    public Duration getDuration() {return Duration; }
    public LocalTime getIdealExecutionTime() { return IdealExecutionTime; }
    public Integer getDays() {return Days; }
    public String getDescription() { return Description; }
    public Integer getDepartmentId() { return DepartmentId; }
    public Integer getPrioritization() { return Prioritization; }
    public Boolean getRepeat() { return Repeat; }
    public LocalDate getLastCompletionDate() { return LastCompletionDate; }
    public LocalTime getLastCompletionTime() { return LastCompletionTime; }


    @Ignore
    private String TimeString;
    @Ignore
    private Integer DaysLeft;
    @Ignore
    private String DepartmentName;
    @Ignore
    private Integer Color;

    public void setTimeString(String timeString) { TimeString = timeString; }
    public void setDaysLeft(Integer daysLeft) { DaysLeft = daysLeft; }
    public void setDepartmentName(String departmentName) { DepartmentName = departmentName; }
    public void setColor(Integer color) { Color = color; }

    public String getTimeString() { return TimeString; }
    public Integer getDaysLeft() { return DaysLeft; }
    public String getDepartmentName() { return DepartmentName; }
    public Integer getColor() { return Color; }
}

