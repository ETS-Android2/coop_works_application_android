package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SchedulePreferences")
public class SchedulePreferenceObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private Integer PrefDays;
    private Integer MaxDays;
    private Integer UserId;

    public SchedulePreferenceObject(Integer Id, Integer PrefDays, Integer MaxDays, Integer UserId){
        this.Id = Id;
        this.PrefDays = PrefDays;
        this.MaxDays = MaxDays;
        this.UserId = UserId;
    }

    public void setId(Integer id) { Id = id; }
    public void setPrefDays(Integer prefDays) { PrefDays = prefDays; }
    public void setMaxDays(Integer maxDays) { MaxDays = maxDays; }

    public Integer getId() { return Id; }
    public Integer getPrefDays() { return PrefDays; }
    public Integer getMaxDays() { return MaxDays; }
    public Integer getUserId() { return UserId; }
}
