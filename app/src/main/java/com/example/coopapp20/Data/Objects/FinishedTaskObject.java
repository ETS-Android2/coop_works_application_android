package com.example.coopapp20.Data.Objects;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.coopapp20.zOtherFiles.DurationFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(tableName = "FinishedTasks", ignoredColumns = {"Title","TimeString","DateTimeString","Divider"})
public class FinishedTaskObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private LocalDate CompletionDate;
    private LocalTime CompletionTime;
    private Duration Duration;
    private Integer Evaluation;
    private String Comment;
    private Integer UserId;
    private Integer TaskId;

    @Embedded(prefix = "emb_")
    private TaskObject Task;

    public FinishedTaskObject(Integer Id, LocalDate CompletionDate, LocalTime CompletionTime, Duration Duration, Integer Evaluation, String Comment, Integer UserId, Integer TaskId, TaskObject Task){
        this.Id = Id;
        this.CompletionDate = CompletionDate;
        this.CompletionTime = CompletionTime;
        this.Duration = Duration;
        this.Evaluation = Evaluation;
        this.Comment = Comment;
        this.UserId = UserId;
        this.TaskId = TaskId;
        this.Task = Task;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getId() {return Id;}
    public LocalDate getCompletionDate() { return CompletionDate; }
    public LocalTime getCompletionTime() { return CompletionTime; }
    public Duration getDuration() { return Duration; }
    public Integer getEvaluation() {return Evaluation;}
    public String getComment() {return Comment;}
    public Integer getUserId() {return UserId;}
    public Integer getTaskId() {return TaskId;}
    public TaskObject getTask() { return Task; }

    private String Title;
    private String TimeString;
    private String DateTimeString;
    private Boolean Divider;

    public void setExtraValues(String title, String timeString, String dateTimeString, boolean divider){
        Title = title;
        TimeString = timeString;
        DateTimeString = dateTimeString;
        Divider = divider;
    }

    public String getTitle() { return Title; }
    public String getTimeString() { return TimeString; }
    public String getDateTimeString() { return DateTimeString; }
    public Boolean getDivider() { return Divider; }
}
