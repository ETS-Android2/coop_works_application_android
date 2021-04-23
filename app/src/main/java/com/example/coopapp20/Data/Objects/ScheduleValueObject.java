package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "ScheduleValues")
public class ScheduleValueObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private LocalDate PreferenceDeadline;
    private LocalDate CreationDeadline;
    private LocalDate ScheduleBeginning;
    private Integer ScheduleWeekDuration;
    private Integer PrefValueOnePoints;
    private Integer PrefValueTwoPoints;
    private Integer PrefValueThreePoints;
    private Integer PrefWorkDaysDistPercent;
    private Integer MedianPointsDistPercent;
    private Boolean ScheduleOngoing;
    private Integer DepartmentId;

    public ScheduleValueObject(Integer Id, LocalDate PreferenceDeadline, LocalDate CreationDeadline, LocalDate ScheduleBeginning, Integer ScheduleWeekDuration,  Integer PrefValueOnePoints, Integer PrefValueTwoPoints, Integer PrefValueThreePoints, Integer PrefWorkDaysDistPercent, Integer MedianPointsDistPercent,Boolean ScheduleOngoing, Integer DepartmentId) {
        this.Id = Id;
        this.PreferenceDeadline = PreferenceDeadline;
        this.CreationDeadline = CreationDeadline;
        this.ScheduleBeginning = ScheduleBeginning;
        this.ScheduleWeekDuration  =ScheduleWeekDuration;
        this.PrefValueOnePoints = PrefValueOnePoints;
        this.PrefValueTwoPoints = PrefValueTwoPoints;
        this.PrefValueThreePoints = PrefValueThreePoints;
        this.PrefWorkDaysDistPercent = PrefWorkDaysDistPercent;
        this.MedianPointsDistPercent = MedianPointsDistPercent;
        this.ScheduleOngoing = ScheduleOngoing;
        this.DepartmentId = DepartmentId;
    }

    public void setId(Integer id) { Id = id; }
    public void setPreferenceDeadline(LocalDate date){PreferenceDeadline = date;}
    public void setCreationDeadline(LocalDate date){CreationDeadline = date;}
    public void setScheduleBeginning(LocalDate date){ScheduleBeginning = date;}
    public void setScheduleWeekDuration(int weeks){ScheduleWeekDuration = weeks;}

    public Integer getId() { return Id; }
    public LocalDate getPreferenceDeadline() { return PreferenceDeadline; }
    public LocalDate getCreationDeadline() { return CreationDeadline; }
    public LocalDate getScheduleBeginning() { return ScheduleBeginning; }
    public Integer getScheduleWeekDuration() { return ScheduleWeekDuration; }
    public Integer getPrefValueOnePoints() { return PrefValueOnePoints; }
    public Integer getPrefValueTwoPoints() { return PrefValueTwoPoints; }
    public Integer getPrefValueThreePoints() { return PrefValueThreePoints; }
    public Integer getPrefWorkDaysDistPercent() { return PrefWorkDaysDistPercent; }
    public Integer getMedianPointsDistPercent() { return MedianPointsDistPercent; }
    public Boolean getScheduleOngoing() { return ScheduleOngoing; }
    public Integer getDepartmentId() { return DepartmentId; }
}
