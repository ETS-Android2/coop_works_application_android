package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "OffDayRequests", ignoredColumns = {"TypeString","DateString","Color"})
public class OffDayRequestObject {

    public static final int TYPE_OFFDAY = 0;
    public static final int TYPE_VACATION = 1;
    public static final int STATUS_DENIED = 0;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_ACCEPTED = 2;

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private Integer Type;
    private LocalDate StartDate;
    private LocalDate EndDate;
    private Integer Reason;
    private String Comment;
    private Integer UserId;
    private Integer Status;

    public OffDayRequestObject(Integer Id, Integer Type, LocalDate StartDate, LocalDate EndDate, Integer Reason, String Comment, Integer UserId, Integer Status){
        this.Id = Id;
        this.Type = Type;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.Reason = Reason;
        this.Comment = Comment;
        this.UserId = UserId;
        this.Status = Status;
    }

    public void setId(Integer id) { Id = id; }

    public void setComment(String comment) { Comment = comment; }
    public void setStartDate(LocalDate startDate) { StartDate = startDate; }
    public void setEndDate(LocalDate endDate) { EndDate = endDate; }

    public Integer getId() { return Id; }
    public Integer getType() { return Type; }
    public LocalDate getStartDate() { return StartDate; }
    public LocalDate getEndDate() { return EndDate; }
    public Integer getReason() { return Reason; }
    public String getComment() { return Comment; }
    public Integer getUserId() { return UserId; }
    public Integer getStatus() { return Status; }


    private String TypeString;
    private String DateString;
    private int Color;

    public void setTypeString(String typeString) { TypeString = typeString; }
    public void setDateString(String dateString) { DateString = dateString; }
    public void setColor(int color) { Color = color; }

    public String getTypeString() { return TypeString; }
    public String getDateString() { return DateString; }
    public int getColor() { return Color; }
}
